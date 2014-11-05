package com.hehua.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.jms.JmsApi;
import com.hehua.freeorder.dao.FreeOrderDAO;
import com.hehua.order.dao.DeliveryCompanyDAO;
import com.hehua.order.dao.OrderTraceDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.exceptions.HttpStatusCodeException;
import com.hehua.order.httpclient.MyHttpClient;
import com.hehua.order.info.AddressInfo;
import com.hehua.order.info.DeliveryCompanyInfo;
import com.hehua.order.info.DeliveryInfo;
import com.hehua.order.info.TraceInfo;
import com.hehua.order.model.DeliveryCompanyModel;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.user.domain.Address;
import com.hehua.user.service.LocationManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-13.
 */
@Named
public class DeliveryService {

    public static final String KUAIDI_100_URL = "http://www.kuaidiapi.cn/rest/?";
    public static final String KUAIDI_100_ACCOUNT = "hehuababy";
    public static final String KUAIDI_100_PASSWD = "001393";
    public static final String KUAIDI_100_UID = "16470";
    public static final String KUAIDI_100_KEY = "819b8b408fe14391805b8be0c2028b96";

    public static final String queryUrl =  KUAIDI_100_URL
            + "uid=" + KUAIDI_100_UID
            + "&key=" + KUAIDI_100_KEY
            + "&order=#{deliveryNum}"
            + "&id=#{deliveryCompanyCode}"
            + "&time=#{time}"
            + "&issign=false"
            + "&ord=desc"
            + "&show=json"
            + "&last=false";

    private Logger log = Logger.getLogger(DeliveryService.class.getName());

    @Inject
    private DeliveryCompanyDAO deliveryCompanyDAO;

    @Inject
    private LocationManager locationManager;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private FreeOrderDAO freeOrderDAO;

    @Inject
    private OrdersService ordersService;

    @Inject
    private OrderTraceDAO orderTraceDAO;

    @Inject
    private OrderTraceService orderTraceService;

    @Inject
    private JmsApi jmsApi;

    /**
     * 去快递100查询快递跟踪信息
     * @param deliveryInfo
     * @return
     */
    public boolean query(DeliveryInfo deliveryInfo) {

        /**
         * TODO
         * 若查询到快递为已签收，则需更新订单为已签收。
         * */
        if (StringUtils.isBlank(deliveryInfo.getDeliveryComPinyin()) || StringUtils.isBlank(deliveryInfo.getDeliveryNum())) {
            return false;
        }

        String time = DateUtils.formatDateTime(new Date());
        try {
            time = URLEncoder.encode(time, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("encode error", e);
            return false;
        }
        String queryUrl = DeliveryService.queryUrl.replace("#{deliveryNum}", StringUtils.trim(deliveryInfo.getDeliveryNum()))
                .replace("#{deliveryCompanyCode}", deliveryInfo.getDeliveryComPinyin())
                .replace("#{time}", time);

        MyHttpClient httpClient = new MyHttpClient();
        String response = null;
        try {
            response = httpClient.httpGet(queryUrl);
        } catch (IOException e) {
            log.error("query error", e);
        } catch (HttpStatusCodeException e) {
            log.error("query error", e);
        }
        if (response == null) {
            return false;
        }
        if (log.isDebugEnabled()) {
            System.out.println("\nkuaidi100 response:" + response + "\n");
        }
        log.info("kuaidi100 response:" + response);
        JSONObject jo = JSON.parseObject(response);
        if (jo == null) {
            return false;
        }
        deliveryInfo.setDeliveryCompany(jo.getString("name"));
        JSONArray ja = jo.getJSONArray("data");
        if (ja == null || jo.get("status") == null) {
            return false;
        }
        deliveryInfo.setStatus(Integer.parseInt(jo.getString("status")));
        List<TraceInfo> traceInfos = new ArrayList<>();
        for (int i = 0; i< ja.size(); i++) {
            JSONObject event = ja.getJSONObject(i);
            TraceInfo traceInfo = new TraceInfo();
            traceInfo.setEvent(event.getString("content"));
            traceInfo.setTime(event.getString("time"));
            traceInfos.add(traceInfo);
        }
        deliveryInfo.setTraceInfo(traceInfos);
        return true;
    }

    /**
     * 获取支持的物流公司列表
     * @return
     */
    public List<DeliveryCompanyInfo> getSupportedComapnyList() {

        return deliveryCompanyDAO.getSupportedList();

    }

    public DeliveryCompanyModel getCompanyByPinyin(String pinyin) {
        return deliveryCompanyDAO.getByPinyin(pinyin);
    }

    /**
     * 获取所有物流公司
     * @return
     */
    public List<DeliveryCompanyModel> getAllDeliveryCompany() {
        return deliveryCompanyDAO.getAllBySort();
    }

    /**
     * 更新物流公司前端显示优先级
     * @param m
     * @return
     */
    public int setDeliveryCompanySort(DeliveryCompanyModel m) {
        return deliveryCompanyDAO.updateSortById(m);
    }

    /**
     * 拼装地址信息
     * @param address
     * @return
     */
    public AddressInfo getAddressInfo(Address address) {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(address.getId());
        addressInfo.setName(address.getReceiver());
        addressInfo.setPhone(address.getMobile());
        addressInfo.setProvince(locationManager.getProvince(address.getProvince()).getName());
        addressInfo.setCity(locationManager.getCity(address.getCity()).getName());
        addressInfo.setCounty(locationManager.getCounty(address.getCounty()).getName());
        addressInfo.setDetail(address.getDetail());
        addressInfo.setPostCode(address.getPostcode());
        addressInfo.setIsDefault(address.getIsdefault());
        return addressInfo;
    }

    /**
     * 接收派友回传的运单号、快递公司信息
     * @param orderid
     * @param deliveryNum
     * @param deliveryCompPinyin
     * @return
     */
    public boolean fillDeliveryInfo(long orderid, int type, String deliveryNum, String deliveryCompPinyin) {

        if (type == OrderTraceModel.TYPE_DEFAULT && ordersDAO.hasWithId(orderid) == null
            || type == OrderTraceModel.TYPE_FREE && freeOrderDAO.hasWithId(orderid) == null) {
            return false;
        }

        DeliveryCompanyModel deliveryCompanyModel = deliveryCompanyDAO.getByPinyin(deliveryCompPinyin);
        if (deliveryCompanyModel == null) {
            return false;
        }

        //TODO
        OrderTraceModel orderTrace = orderTraceDAO.getByOrderIdAndType(orderid, type);
        if (orderTrace != null) {
            if (orderTrace.getStatus() == OrderTraceModel.STATUS_NEW) {
                if (deliveryCompPinyin.equals(orderTrace.getDeliveryCompanPinyin()) && deliveryNum.equals(orderTrace.getDeliveryNum())) {
                    log.info("dupclicate deliveryinfo:" + orderTrace.getId());
                } else {
                    log.info("change deliveryinfo,id=" + orderTrace.getId() + " old company=" + orderTrace.getDeliveryCompanPinyin() + " old num=" + orderTrace.getDeliveryNum());
                    orderTrace.setDeliveryNum(deliveryNum);
                    orderTrace.setDeliveryCompanPinyin(deliveryCompPinyin);
                    orderTrace.setModtime(DateUtils.unix_timestamp());
                    orderTraceDAO.updateDeliveryInfo(orderTrace);
                }
                return true;
            } else {
                log.error("delivery info already exist, id=" + orderTrace.getId());
                return true;
            }
        } else {
            OrderTraceModel orderTraceModel = new OrderTraceModel();
            orderTraceModel.setOrderid(orderid);
            orderTraceModel.setType(type);
            orderTraceModel.setDeliveryCompanPinyin(deliveryCompPinyin);
            orderTraceModel.setDeliveryNum(deliveryNum);
            orderTraceModel.initWhenAdd();
            orderTraceDAO.add(orderTraceModel);
            if (orderTraceService.setOrderDeliveried(orderTraceModel)) {
                log.info("set order deliveried, orderid=" + orderid + ",type=" + type);
            } else {
                log.error("set order deliveried failed, orderid=" + orderid + ",type=" + type);
            }
            //加入查询队列
            jmsApi.call("QueryKuaidi100", orderTraceModel.getId(), OrderTraceService.QUERY_INTERVAL);
            log.info("put is back to queue, id=" + orderTraceModel.getId() + ",interval=" + OrderTraceService.QUERY_INTERVAL);

        }
        return true;
    }

    public JSONObject updateSkuQuantity() {
        JSONObject jo = new JSONObject();
        return jo;
    }
}
