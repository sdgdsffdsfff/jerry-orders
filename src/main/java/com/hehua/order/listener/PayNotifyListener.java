package com.hehua.order.listener;

import com.hehua.framework.jms.JmsApi;
import com.hehua.item.service.ItemService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.dao.NotifyDAO;
import com.hehua.order.dao.NotifySuccessLogDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.exceptions.NoSuchPayConfigException;
import com.hehua.order.model.*;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.pay.PayManager;
import com.hehua.order.service.CreditService;
import com.hehua.order.service.OrderService;
import com.hehua.order.service.RefundService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-14.
 * 支付通知异步处理脚本
 */
@Named
public class PayNotifyListener implements MessageListener {

    private Logger log = Logger.getLogger(PayNotifyListener.class.getName());

    @Inject
    private NotifyDAO notifyDAO;

    @Inject
    private NotifySuccessLogDAO notifySuccessLogDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private CreditService creditService;

    @Inject
    private OrderService orderService;

    @Inject
    private ItemSkuService itemSkuService;

    @Inject
    private ItemService itemService;

    @Inject
    private PayManager payManager;

    @Inject
    private RefundService refundService;

    @Inject
    private JmsApi jmsApi;

    @Autowired
    @Qualifier("orderTransactionManager")
    private DataSourceTransactionManager orderTxManager;

    @Autowired
    @Qualifier("itemTransactionManager")
    private DataSourceTransactionManager itemTxManager;

    private TransactionStatus orderTxStatus;
    private TransactionStatus itemTxStatus;

    private void initOrderTransaction() {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        orderTxStatus = orderTxManager.getTransaction(txDef);
    }

    private void initItemTransaction() {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        itemTxStatus = itemTxManager.getTransaction(txDef);
    }

    private boolean processRecharge(NotifyModel notify) {
        log.error("type error:" + notify.toString());
        return false;
    }

    private boolean processDirect(NotifyModel notify) {

        OrdersModel order = ordersDAO.getById(notify.getObjid());
        if (order == null) {
            log.error("order not exists:" + notify.toString());
            return false;
        }

        if (!creditService.updateCheckAndCreate(order.getUserid())) {
            log.info("create user credit,userid=" + order.getUserid());
        }

        if (!creditService.pay(notify)) {
            log.error("pay error:" + notify.toString());
            return false;
        } else {
            log.info("pay success:" + notify.toString());
        }

        if (!orderService.buy(notify)) {
            log.error("buy failed:" + notify.toString());
            return false;
        } else {
            log.info("buy success:" + notify.toString());
        }
        return true;
    }

    public void processPay(long notifyid) throws RuntimeException {

        if (log.isDebugEnabled()) {
            System.out.println("get notifyid : " + notifyid);
        }

        NotifyModel notifyModel = notifyDAO.getById(notifyid);
        if (notifyModel == null) {
            log.error("notifylog not exists:" + notifyid);
            return;
        }
        if (notifyModel.getStatus() != 0) {
            log.error("notifylog status error:" + notifyid);
            return;
        }

        //初始化事务
        this.initOrderTransaction();

        //判断是否重复通知
        if (notifySuccessLogDAO.getByOutnoAndPaytype(notifyModel.getOutno(), notifyModel.getPaytype()) != null) {
            log.error("duplicate notifylog:" + notifyModel.toString());
            notifyDAO.setFail(notifyid, "duplicate notifylog");
            orderTxManager.commit(orderTxStatus);
            return;
        }
        //检查交易状态是否合法
        BasePayProvider payProvider = null;
        try {
            payProvider = (BasePayProvider)payManager.getPayObj(notifyModel.getPaytype());
        } catch (NoSuchPayConfigException e) {
            log.error("pay type error:" + notifyModel.toString());
            notifyDAO.setFail(notifyid, "paytype error");
            orderTxManager.commit(orderTxStatus);
            return;
        }
        if (!payProvider.checkTradeStatus(notifyModel)) {
            log.error("trade status error:" + notifyModel.toString());
            notifyDAO.setFail(notifyid, "trade status error");
            orderTxManager.commit(orderTxStatus);
            return;
        }

        boolean processResult;
        long userid = 0;
        if (notifyModel.getType() == NotifyModel.TYPE_CHARGE) {
            processResult = this.processRecharge(notifyModel);
            userid = notifyModel.getObjid();
        } else {
            processResult =this.processDirect(notifyModel);
            OrdersModel order = ordersDAO.getById(notifyModel.getObjid());
            if (order == null) {
                log.error("order not exists:" + notifyModel.toString());
                orderTxManager.rollback(orderTxStatus);
                notifyDAO.setFail(notifyid, "order not exists");
                return;
            }
            userid = order.getUserid();
        }
        if (!processResult) {
            log.error("process pay failed:" + notifyModel.toString());
        }

        //处理成功，增加notifylog处理成功记录
        NotifySuccessLogModel notifySuccessLogModel = new NotifySuccessLogModel();
        notifySuccessLogModel.setUserid(userid);
        notifySuccessLogModel.initWhenAdd(notifyModel);
        notifySuccessLogDAO.add(notifySuccessLogModel);
        notifyDAO.setSuccess(notifyid);

        orderTxManager.commit(orderTxStatus);

        //同步至派友
        jmsApi.call("SyncOrderToPaiu", "n-" + notifyModel.getObjid());

        //改为下单成功减库存
        /*
        this.initItemTransaction();
        if (notifyModel.getType() == NotifyModel.TYPE_PAY && this.updateItemQuantity(notifyModel.getObjid())) {
            itemTxManager.commit(itemTxStatus);
            jmsApi.call("SyncOrderToPaiu", notifyModel.getObjid());
        } else {
            //更新商品数量失败
            itemTxManager.rollback(itemTxStatus);
            if (refundService.updatePayFailApply(notifySuccessLogModel)) {
                log.info("refund success:" + notifyModel.toString());
            } else {
                log.error("refund fail:" + notifyModel.toString());
            }
        }
        */
    }


    /**
     * 更新item销量，sku库存数量
     * @param orderid
     * @return
     */
    public boolean updateItemQuantity(long orderid) {
        List<OrderItemsModel> orderItemsModelList = orderItemsDAO.getListByOrderId(orderid);
        for (OrderItemsModel orderItemsModel : orderItemsModelList) {
            if (!itemSkuService.decrSkuQuantity((int)orderItemsModel.getSkuid(), orderItemsModel.getQuantity())) {
                log.error("decrease sku quantity error, skuid=" + orderItemsModel.getSkuid() + ", quantity=" + orderItemsModel.getQuantity());
                return false;
            }
            if (!itemService.incrItemSales((int)orderItemsModel.getItemid(), orderItemsModel.getQuantity())) {
                log.error("increase item sales error, skuid=" + orderItemsModel.getSkuid() + ", quantity=" + orderItemsModel.getQuantity());
                return false;
            }
            log.info("decrease sku quantity, skuid=" + orderItemsModel.getSkuid() + ", quantity=" + orderItemsModel.getQuantity());
        }
        return true;
    }

    @Override
    public void onMessage(Message m) {
        if (m instanceof ObjectMessage || m instanceof ActiveMQTextMessage) {
            long notifyid = 0;
            try {
                if (m instanceof ObjectMessage) {
                    notifyid = (Long) ((ObjectMessage) m).getObject();
                } else {
                    notifyid = Long.parseLong(((ActiveMQTextMessage)m).getText());
                }
                this.processPay(notifyid);
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
