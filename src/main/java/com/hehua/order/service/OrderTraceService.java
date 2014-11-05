package com.hehua.order.service;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.time.DateUtils;
import com.hehua.freeorder.dao.FreeOrderDAO;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.freeorder.service.FreeOrderService;
import com.hehua.order.dao.DeliveryCompanyDAO;
import com.hehua.order.dao.OrderTraceDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.info.DeliveryInfo;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.order.model.OrdersModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

/**
 * Created by liuweiwei on 14-9-1.
 */
@Named
public class OrderTraceService {

    @Inject
    private OrderTraceDAO orderTraceDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private FreeOrderDAO freeOrderDAO;

    @Inject
    private DeliveryCompanyDAO deliveryCompanyDAO;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private OrdersService ordersService;

    @Inject
    private FreeOrderService freeOrderService;

    public static final int QUERY_INTERVAL = 7200*1000;

    private Logger log = Logger.getLogger(DeliveryService.class.getName());

    /**
     * 生成物流信息
     * @param orderid
     * @return
     */
    public DeliveryInfo updateGenDeliveryInfo(long orderid, int type) {
        DeliveryInfo deliveryInfo = new DeliveryInfo();

        //TODO
        OrderTraceModel orderTrace = orderTraceDAO.getByOrderIdAndType(orderid, type);
        if (orderTrace == null) {
            return deliveryInfo;
        }
        deliveryInfo.setDeliveryCompany(deliveryCompanyDAO.getByPinyin(orderTrace.getDeliveryCompanPinyin()).getName());
        deliveryInfo.setDeliveryComPinyin(orderTrace.getDeliveryCompanPinyin());
        deliveryInfo.setDeliveryNum(orderTrace.getDeliveryNum());
        //已签收，不再查询
        if (orderTrace.getStatus() == OrderTraceModel.STATUS_SIGNED) {
            deliveryInfo.setTraceInfo(JSON.parseObject(orderTrace.getData(), DeliveryInfo.class).getTraceInfo());
        } else {
            //距离上次查询在两小时之内，直接返回
            if (orderTrace.getLastqtime() !=0 && (DateUtils.unix_timestamp() - orderTrace.getLastqtime())*1000 < OrderTraceService.QUERY_INTERVAL) {
                if (StringUtils.isBlank(orderTrace.getData())) {
                    deliveryInfo.setTraceInfo(Collections.EMPTY_LIST);
                } else {
                    deliveryInfo.setTraceInfo(JSON.parseObject(orderTrace.getData(), DeliveryInfo.class).getTraceInfo());
                }
                log.info("less than 2 hours, return directly, id=" + orderTrace.getId());
            } else {
                //否则去快递100查询，更新本次查询结果
                if (deliveryService.query(deliveryInfo)) {
                    if (deliveryInfo.getStatus() == 4) {
                        orderTrace.setStatus(OrderTraceModel.STATUS_SIGNED);
                        //更新订单为已签收
                        this.setOrderSigned(orderTrace);
                        log.info("query signed, id=" + orderTrace.getId());
                    } else if (deliveryInfo.getStatus() >= 1 && deliveryInfo.getStatus() <= 3) {
                        orderTrace.setStatus(OrderTraceModel.STATUS_OK);
                        log.info("query in processing, id=" + orderTrace.getId());
                    }
                    orderTrace.setLastqtime(DateUtils.unix_timestamp());
                    orderTrace.setQuerytimes(orderTrace.getQuerytimes() + 1);
                    orderTrace.setData(JSON.toJSONString(deliveryInfo));
                    orderTrace.setModtime(DateUtils.unix_timestamp());
                    orderTraceDAO.queryKuaidiApi(orderTrace);
                }
            }
        }
        return deliveryInfo;
    }

    public boolean setOrderSigned(OrderTraceModel trace) {
        if (trace.getType() == OrderTraceModel.TYPE_DEFAULT) {
            OrdersModel order = ordersDAO.getById(trace.getOrderid());
            if (order == null) {
                return false;
            }
            return ordersService.setSigned(order);
        } else {
            FreeOrderModel order = freeOrderDAO.getById((int)trace.getOrderid());
            if (order == null) {
                return false;
            }
            freeOrderService.setSigned(order);
        }
        return true;
    }

    public boolean setOrderDeliveried(OrderTraceModel trace) {
        if (trace.getType() == OrderTraceModel.TYPE_DEFAULT) {
            OrdersModel order = ordersDAO.getById(trace.getOrderid());
            if (order == null) {
                return false;
            }
            if (!order.isStatusPayed()) {
                return false;
            }
            return ordersService.setDeliveried(order);
        } else {
            FreeOrderModel order = freeOrderDAO.getById((int)trace.getOrderid());
            if (order == null) {
                return false;
            }
            if (!order.isStatusApprove1()) {
                return false;
            }
            return freeOrderService.setDeliveried(order);
        }
    }
}
