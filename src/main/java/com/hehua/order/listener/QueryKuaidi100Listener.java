package com.hehua.order.listener;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.jms.JmsApi;
import com.hehua.order.dao.OrderTraceDAO;
import com.hehua.order.info.DeliveryInfo;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.order.service.DeliveryService;
import com.hehua.order.service.OrderTraceService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by liuweiwei on 14-9-2.
 */
@Named
public class QueryKuaidi100Listener implements MessageListener {

    private Logger log = Logger.getLogger(DeliveryService.class.getName());

    @Inject
    private OrderTraceDAO orderTraceDAO;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private OrderTraceService orderTraceService;

    @Inject
    private JmsApi jmsApi;

    private void queryKuaidi100(int traceId) {
        OrderTraceModel trace = orderTraceDAO.getById(traceId);
        if (trace == null) {
            log.error("trace not exists, id=" + traceId);
            return;
        }
        if (trace.getStatus() == OrderTraceModel.STATUS_SIGNED) {
            log.info("trace has been signed, id=" + traceId);
            return;
        }
        //若上次查询时间与本次查询时间间隔小于两小时
        long interval = (DateUtils.unix_timestamp() - trace.getLastqtime()) * 1000;
        if (trace.getLastqtime() != 0 && interval < OrderTraceService.QUERY_INTERVAL) {
            jmsApi.call("QueryKuaidi100", traceId, OrderTraceService.QUERY_INTERVAL - interval);
            log.info("query interval less than 2 hours, put back to queue, id=" + traceId);
            return;
        }
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setDeliveryNum(trace.getDeliveryNum());
        deliveryInfo.setDeliveryComPinyin(trace.getDeliveryCompanPinyin());
        if (deliveryService.query(deliveryInfo)) {
            if (deliveryInfo.getStatus() == 4) {
                trace.setStatus(OrderTraceModel.STATUS_SIGNED);
                //更新订单为已签收
                if (!orderTraceService.setOrderSigned(trace)) {
                    log.error("set order signed failed, traceid=" + trace.getId());
                }
                log.info("query return signed, id=" + traceId);
            } else if (deliveryInfo.getStatus() <= 3 && deliveryInfo.getStatus() >= 1) {
                trace.setStatus(OrderTraceModel.STATUS_OK);
                //还没签收，继续放入队列，等待下次查询
                jmsApi.call("QueryKuaidi100", traceId, OrderTraceService.QUERY_INTERVAL);
                log.info("put is back to queue, id=" + traceId + ",interval=" + OrderTraceService.QUERY_INTERVAL);
            }
            trace.setLastqtime(DateUtils.unix_timestamp());
            trace.setQuerytimes(trace.getQuerytimes() + 1);
            trace.setData(JSON.toJSONString(deliveryInfo));
            trace.setModtime(DateUtils.unix_timestamp());
            orderTraceDAO.queryKuaidiApi(trace);
        } else {
            log.error("query error, id=" + traceId);
        }
    }

    @Override
    public void onMessage(Message m) {
        if (m instanceof ObjectMessage || m instanceof ActiveMQTextMessage) {
            int traceId = 0;
            try {
                if (m instanceof ObjectMessage) {
                    traceId = (Integer) ((ObjectMessage) m).getObject();
                } else {
                    traceId = Integer.parseInt(((ActiveMQTextMessage) m).getText());
                }
                this.queryKuaidi100(traceId);
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
