package com.hehua.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hehua.commons.time.DateUtils;
import com.hehua.item.domain.Item;
import com.hehua.item.domain.ItemSku;
import com.hehua.item.service.ItemPropertyRender;
import com.hehua.item.service.ItemService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.constants.PaiyouConfig;
import com.hehua.order.dao.DeliveryInfoDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.exceptions.HttpStatusCodeException;
import com.hehua.order.httpclient.MyHttpClient;
import com.hehua.order.info.AddressInfo;
import com.hehua.order.info.paiyou.GoodsInfo;
import com.hehua.order.info.paiyou.OrderData;
import com.hehua.order.info.paiyou.OrderInfo;
import com.hehua.order.model.DeliveryInfoModel;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.service.OrdersService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-25.
 * 支付成功订单发送至派友
 */
@Named
public class SyncOrderToPaiuListener implements MessageListener {

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private OrdersService ordersService;

    @Inject
    private DeliveryInfoDAO deliveryInfoDAO;

    @Inject
    private ItemSkuService itemSkuService;

    @Inject
    private ItemService itemService;

    @Inject
    private ItemPropertyRender itemPropertyRender;

    private static Logger log = Logger.getLogger(SyncOrderToPaiuListener.class.getName());

    /**
     * 将订单信息发送至派友
     * @param orderid
     */
    private boolean sendToPaiu(long orderid, String type) {
        if (!PaiyouConfig.isSwitchOn()) {
            log.info("paiyou switch off, orderid=" + orderid);
            return false;
        }
        if (!type.equals("n")) {
            log.info("only send normal orders!, orderid=" + orderid);
            return false;
        }
        OrdersModel order = ordersDAO.getById(orderid);
        if (order == null) {
            log.error("order not exists, id=" + orderid);
            return false;
        }
        if (order.isSendedToPaiu()) {
            log.error("order has sended to paiu, id=" + orderid);
            return false;
        }

        OrderInfo orderInfo = this.genOrderInfo(order);
        if (orderInfo == null) {
            log.error("orderinfo null, id=" + orderid);
            return false;
        }
        OrderData orderData = new OrderData();
        orderData.setOrderInfo(orderInfo);
        String strData = JSON.toJSONString(orderData);
        String body = "appkey=" + PaiyouConfig.APPKEY + "&appsecret=" + PaiyouConfig.APPSECKET + "&data=" + strData;
        if (log.isDebugEnabled()) {
            System.out.println("send to paiu, body:" + body);
        }
        log.info("send to paiu, body:" + body);
        MyHttpClient myHttpClient = new MyHttpClient();
        String response = null;

        try {
            response = myHttpClient.httpPost(PaiyouConfig.getSyncOrderUrl(), body);
        } catch (IOException e) {
            log.error("io exception, id=" + orderid, e);
            throw new RuntimeException(e);
        } catch (HttpStatusCodeException e) {
            log.error("network error, id=" + orderid, e);
            //put it back to queue
            throw new RuntimeException(e);
        }

        log.info("paiu response, body:" + response);
        if (log.isDebugEnabled()) {
            System.out.println("paiu response, body=" + response);
        }
        JSONObject jo = JSON.parseObject(response);
        if (jo.get("state").equals("true")) {
            //设置订单为已发送至派友
            ordersService.setSendedToPaiu(order);
            return true;
        }
        log.error("send to paiyou error, id=" + orderid + ", reason=" + jo.get("reason"));
        if (log.isDebugEnabled()) {
            System.out.println("send to paiyou error, id=" + orderid + ", reason=" + jo.get("reason"));
        }
        return false;
    }

    /**
     * 生成派友需要的订单信息
     * @param order
     * @return
     */
    private OrderInfo genOrderInfo(OrdersModel order) {

        List<OrderItemsModel> orderItemsModels = orderItemsDAO.getListByOrderId(order.getId());

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderCode("n-" + String.valueOf(order.getId()));
        orderInfo.setCreateTime(DateUtils.formatTimestamp(order.getOrdertime(), false));
        //这个字段任意传
        orderInfo.setOrderStatus(String.valueOf(order.getStatus()));

        DeliveryInfoModel deliveryInfoModel = deliveryInfoDAO.getByOrderId(order.getId());
        AddressInfo addressInfo = (AddressInfo) JSON.parseObject(deliveryInfoModel.getAddress(), AddressInfo.class);
        orderInfo.setConsigneeUser(addressInfo.getName());
        orderInfo.setConsigneeZip(addressInfo.getPostCode());
        orderInfo.setConsigneePhone(addressInfo.getPhone());
        orderInfo.setConsigneeMobile(addressInfo.getPhone());
        orderInfo.setConsigneeProvince(addressInfo.getProvince());
        orderInfo.setConsigneeCity(addressInfo.getCity());
        orderInfo.setConsigneeDistrict(addressInfo.getCounty());
        orderInfo.setConsigneeAddress(addressInfo.getDetail());
        orderInfo.setPayment(order.getTotalfee().doubleValue());
        orderInfo.setPayType("0"); //先付款
        // 派友不支持开发票
        orderInfo.setHasInvoice("0");

        orderInfo.setBuyerMemo("");
        orderInfo.setSellerMemo("");

        List<GoodsInfo> goodInfos = new ArrayList<>();
        int totalNum = 0;
        for (OrderItemsModel orderItemsModel : orderItemsModels) {
            GoodsInfo goodInfo = new GoodsInfo();
            Item item = itemService.getItemById(orderItemsModel.getItemid());
            ItemSku itemSku = itemSkuService.getItemSkuById(orderItemsModel.getSkuid());
            goodInfo.setSalePrice(itemSku.getRealprice());
            goodInfo.setGoodsNum(orderItemsModel.getQuantity());
            goodInfo.setGoodsName(item.getName());
            goodInfo.setBarcode(itemSku.getBarcode());
            goodInfo.setAttribute(itemPropertyRender.renderPropertiesStr(itemSku));
            goodInfos.add(goodInfo);
            totalNum += orderItemsModel.getQuantity();
        }
        orderInfo.setTotalNum(totalNum);
        orderInfo.setGoodInfo(goodInfos);

        return orderInfo;
    }

    @Override
    public void onMessage(Message m) {
        if (m instanceof ObjectMessage || m instanceof ActiveMQTextMessage) {
            try {
                String idStr = "";
                if (m instanceof ObjectMessage) {
                    idStr = (String)((ObjectMessage) m).getObject();
                } else {
                    idStr = ((ActiveMQTextMessage)m).getText();
                }
                log.info("get order, id=" + idStr);
                String[] idInfo = idStr.split("-");
                long orderid = 0;
                String type = "n";
                if (idInfo.length > 1) {
                    orderid = Long.parseLong(idInfo[1]);
                    type = idInfo[0];
                } else {
                    orderid = Long.parseLong(idInfo[0]);
                }
                if (this.sendToPaiu(orderid, type)) {
                    log.debug("send to paiyou successfully, id=" + orderid);
                }
            } catch (JMSException e) {
                log.error("jms exception occured!", e);
                throw new RuntimeException(e);
            } catch (Exception e) {
                log.error("error occured!", e);
                throw new RuntimeException(e);
            }
        } else {
            log.error("message type error:" + m.toString());
        }
    }
}
