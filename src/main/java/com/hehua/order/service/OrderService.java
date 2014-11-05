package com.hehua.order.service;

import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.hehua.commons.Transformers;
import com.hehua.commons.collection.CollectionUtils;
import com.hehua.commons.model.UserAccessInfo;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.log.LogService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.dao.CreditDAO;
import com.hehua.order.dao.CreditLogDAO;
import com.hehua.order.dao.NotifySuccessLogDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.exceptions.CreditException;
import com.hehua.order.info.OrderStatusCountInfo;
import com.hehua.order.listener.PayNotifyListener;
import com.hehua.order.model.CreditLogModel;
import com.hehua.order.model.CreditModel;
import com.hehua.order.model.NotifyModel;
import com.hehua.order.model.NotifySuccessLogModel;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.utils.DecimalUtil;
import com.hehua.user.domain.Baby;
import com.hehua.user.service.BabyService;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuweiwei on 14-7-23. 订单相关业务接口， 仅内部调用
 */
@Named
public class OrderService {

    private Logger log = Logger.getLogger(OrderService.class.getName());

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private CreditDAO creditDAO;

    @Inject
    private CreditLogDAO creditLogDAO;

    @Inject
    private CreditService creditService;

    @Inject
    private BabyService babyService;

    @Inject
    private ItemSkuService itemSkuService;

    @Inject
    private NotifySuccessLogDAO notifySuccessLogDAO;

    /**
     * 获取用户购买账号
     * @param orderid
     * @return
     */
    public String getUserBuyAccount(long orderid) {
        List<NotifySuccessLogModel> logs = notifySuccessLogDAO.getByOrderId(orderid);
        StringBuffer sb = new StringBuffer();
        for (NotifySuccessLogModel log : logs) {
            sb.append("支付方式：");
            sb.append(BasePayProvider.payIdCNameMap.get(log.getPaytype()));
            sb.append("，支付账号：");
            sb.append(log.getBuyer());
        }
        return sb.toString();
    }

    public ListMultimap<Long, OrderItemsModel> getOrderItemByOrderId(List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return null;
        }

        List<OrderItemsModel> orderItemList = orderItemsDAO.getListByOrderIds(orderIds);
        if (CollectionUtils.isEmpty(orderItemList)) {
            return null;
        }

        return Transformers.transformAsListMultimap(orderItemList, new Function<OrderItemsModel, Long>() {
            @Override
            public Long apply(OrderItemsModel input) {
                return input.getOrderid();
            }
        });


    }

    private UserAccessInfo genUserAccessInfo(OrdersModel order) {
        UserAccessInfo accessInfo = new UserAccessInfo();
        accessInfo.setTm(DateUtils.formatDateTime(new Date()));
        Baby baby = babyService.getBabyByUidxUid(order.getUserid());
        accessInfo.setPreganancy(baby.getStatus());
        accessInfo.setBabyGender(baby.getGender());
        if (baby.getBirthday() != null) {
            accessInfo.setBabyBirthday(DateUtils.formatDate(baby.getBirthday()));
        }
        if (baby.getEdc() != null) {
            accessInfo.setEdc(DateUtils.formatDate(baby.getEdc()));
        }
        accessInfo.setUid(order.getUserid());
        accessInfo.setEvent("payreturn");
        accessInfo.setStatus(UserAccessInfo.STATUS_SUCCESS);

        List<OrderItemsModel> orderItems = orderItemsDAO.getListByOrderId(order.getId());
        accessInfo.setCartSelected(orderItems.size());
        int skuQuantity = 0;
        Set<Long> itemids = new HashSet<>();
        for (OrderItemsModel orderItem : orderItems) {
            itemids.add(orderItem.getItemid());
            skuQuantity += orderItem.getQuantity();
        }
        accessInfo.setItemQuantity(itemids.size());
        accessInfo.setSkuQuantity(skuQuantity);
        accessInfo.setOrderid(order.getId());
        accessInfo.setTotolFee(DecimalUtil.toStandard(order.getTotalfee()).doubleValue());
        accessInfo.setDeliveryFee(DecimalUtil.toStandard(order.getDeliveryfee()).doubleValue());
        return accessInfo;
    }

    public List<OrderStatusCountInfo> getCountByStatus(long userid) {
        List<OrderStatusCountInfo> orderStatusCountInfos = ordersDAO.getCountByStatus(userid);
        return orderStatusCountInfos;
    }

    public Map<Integer, Integer> getStatusesMap(long userid) {

        List<OrderStatusCountInfo> countByStatus = getCountByStatus(userid);
        Map<Integer, Integer> statusMap = new HashMap<Integer, Integer>(countByStatus.size());
        for (OrderStatusCountInfo status : countByStatus) {
            statusMap.put(status.getStatus(), status.getCount());
        }
        return statusMap;
    }

    public boolean buy(NotifyModel notify) {
        Logger log = Logger.getLogger(PayNotifyListener.class.getName());
        OrdersModel order = ordersDAO.getById(notify.getObjid());
        if (order == null) {
            log.error("order not exists:" + notify.toString());
            return false;
        }
        UserAccessInfo userAccessInfo = this.genUserAccessInfo(order);
        LogService flumeEventLogger = LogService.getInstance();
        if (order.getStatus() > OrdersModel.STATUS_CLOSED) {
            log.error("order has payed, orderid=" + order.getId() + ", notifylog:"
                    + notify.toString());
            userAccessInfo.setStatus(OrdersErrorEnum.ORDER_STATUS_ERROR.getCode());
            flumeEventLogger.info(userAccessInfo);
            return false;
        }
        CreditModel credit = creditDAO.getByUserid(order.getUserid());
        if (credit == null) {
            log.error("credit not exists:" + notify.toString());
            userAccessInfo.setStatus(OrdersErrorEnum.CREDIT_NOT_EXISTS.getCode());
            flumeEventLogger.info(userAccessInfo);
            return false;
        }

        //设置订单本次支付金额
        order.setPaytype(notify.getPaytype());
        order.setPaytime((int) (System.currentTimeMillis() / 1000L));
        order.setDirect(notify.getMoney().add(order.getDirect()));
        order.setPayed(notify.getMoney().add(order.getPayed()));
        order.setOutmoney(notify.getMoney().add(order.getOutmoney()));

        BigDecimal toPay = order.getTotalfee().subtract(order.getReduce());
        //账户余额不足，需再次支付，基本不会走到这个分支
        if (credit.getValue().subtract(toPay).doubleValue() < 0) {
            log.error("credit less, orderid=" + order.getId() + ", notifylog:" + notify.toString());
            ordersDAO.updatePayed(order);
            userAccessInfo.setStatus(OrdersErrorEnum.CREDIT_LESS_ERROR.getCode());
            flumeEventLogger.info(userAccessInfo);
            return false;
        }
        //账户余额足以支付，减去账户余额，优先级：返利 > 充值卡 > 充值 > 支付
        BigDecimal rebate = new BigDecimal(0);
        BigDecimal giftcard = new BigDecimal(0);
        BigDecimal recharge = new BigDecimal(0);
        BigDecimal direct = new BigDecimal(0);
        if (toPay.doubleValue() > 0.001 && credit.getRebate().doubleValue() > 0.001) {
            if (credit.getRebate().subtract(toPay).doubleValue() > 0.001) {
                rebate = toPay;
            } else {
                rebate = credit.getRebate();
            }
            toPay = toPay.subtract(rebate);
        }
        if (toPay.doubleValue() > 0.001 && credit.getGiftcard().doubleValue() > 0.001) {
            if (credit.getGiftcard().subtract(toPay).doubleValue() > 0.001) {
                giftcard = toPay;
            } else {
                giftcard = credit.getGiftcard();
            }
            toPay = toPay.subtract(giftcard);
        }
        if (toPay.doubleValue() > 0.001 && credit.getRecharge().doubleValue() > 0.001) {
            if (credit.getRecharge().subtract(toPay).doubleValue() > 0.001) {
                recharge = toPay;
            } else {
                recharge = credit.getRecharge();
            }
        }
        if (toPay.doubleValue() > 0.001 && credit.getDirect().doubleValue() > 0.001) {
            if (credit.getDirect().subtract(toPay).doubleValue() > 0.001) {
                direct = toPay;
            } else {
                direct = credit.getDirect();
            }
        }

        BigDecimal balance = null;
        //减去余额
        try {
            balance = creditService.updateUpdate(order.getUserid(), toPay.negate(),
                    rebate.negate(), giftcard.negate(), recharge.negate(), direct.negate());
        } catch (CreditException e) {
            log.error("sql error:", e);
            return false;
        }
        CreditLogModel creditlog = new CreditLogModel();
        creditlog.setUserid(order.getUserid());
        creditlog.setType(CreditLogModel.TYPE_BUY);
        creditlog.setObjid(order.getId());
        creditlog.setValue(toPay.negate());
        creditlog.setBalance(balance);
        creditlog.setRebate(rebate.negate());
        creditlog.setGiftcard(giftcard.negate());
        creditlog.setRecharge(recharge.negate());
        creditlog.setDirect(direct.negate());
        creditlog.setAddtime((int) (System.currentTimeMillis() / 1000L));
        if (creditLogDAO.add(creditlog) != 1) {
            log.error("sql error:" + creditlog.toString());
            return false;
        }

        //更新订单状态
        if (ordersDAO.setPayed(order) != 1) {
            log.error("sql error:" + creditlog.toString());
            return false;
        }
        flumeEventLogger.info(userAccessInfo);
        return true;
    }

    public int getItemCountInUncloseOrders(long userId, int itemId) {
        List<OrderItemsModel> orderItems = orderItemsDAO.getListByUserIdAndItemId(userId, itemId);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(orderItems)) {
            return 0;
        }

        Set<Long> orderIds = new HashSet<>();
        for (OrderItemsModel orderItem : orderItems) {
            orderIds.add(orderItem.getOrderid());
        }

        if (org.apache.commons.collections.CollectionUtils.isEmpty(orderIds)) {
            return 0;
        }

        List<OrdersModel> orders = ordersDAO.getsByIds(orderIds);
        Map<Long, OrdersModel> ordersMap = new HashMap<>();
        for (OrdersModel order : orders) {
            ordersMap.put(order.getId(), order);
        }

        int itemCount = 0;
        for (OrderItemsModel orderItem : orderItems) {
            OrdersModel ordersModel = ordersMap.get(orderItem.getOrderid());
            if (ordersModel == null) {
                continue;
            }
            if (ordersModel.getStatus() != OrdersModel.STATUS_CLOSED) {
                itemCount += orderItem.getQuantity();
            }
        }
        return itemCount;
    }
}
