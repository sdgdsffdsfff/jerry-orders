package com.hehua.order.refund;

import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.NotifySuccessLogDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.dao.RefundDAO;
import com.hehua.order.dao.RefundLogDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.exceptions.NoSuchPayConfigException;
import com.hehua.order.model.NotifySuccessLogModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.model.RefundLogModel;
import com.hehua.order.model.RefundModel;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.pay.PayManager;
import com.hehua.order.refund.params.ResultParam;
import com.hehua.order.service.RefundService;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-9-24.
 * 生成去第三方退款的url
 */
@Named
public class WithdrawAction extends RefundBase<BigDecimal, ResultParam<String>> {

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private PayManager payManager;

    @Inject
    private RefundService refundService;

    @Inject
    private NotifySuccessLogDAO notifySuccessLogDAO;

    @Override
    public int getAction() {
        return this.ACTION_WAITING;
    }

    @Override
    public boolean beforeExecute(RefundModel refund, BigDecimal params, ResultParam<String> result) {
        if (refund == null) {
            result.setMetaCode(RefundErrorEnum.REFUND_NOT_EXISTS);
            return false;
        }
        if (!refund.canWithdraw()) {
            result.setMetaCode(RefundErrorEnum.REFUND_WITHDRAW_STATUS_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, BigDecimal params, ResultParam<String> result) {
        OrdersModel order = ordersDAO.getById(refund.getOrderid());
        BasePayProvider payProvider = null;
        try {
            payProvider = payManager.getPayObj(order.getPaytype());
        } catch (NoSuchPayConfigException e) {
            result.setMetaCode(OrdersErrorEnum.PAY_TYPE_ERROR);
            return false;
        }
        //之前已发送过请求，直接返回退款url;
        if (refund.getSendtime() != 0) {
            /**
             * 退款批次号已经生成，无法确定请求是否已发送至支付宝。
             * 如果是同一天，可以无限点，不会去更新批次号，如果隔天就不能再点了
             */
            if (!DateUtils.isToday(refund.getSendtime(), false)) {
                result.setMetaCode(OrdersErrorEnum.REFUND_OUTNO_EXPIRED);
                return false;
            }
            result.setData(payProvider.refund(refund));
            return true;
        }
        NotifySuccessLogModel notifySuccessLogModel = refundService.getNotifySuccessLogByRefundId(refund.getId());
        if (notifySuccessLogModel == null) {
            result.setMetaCode(OrdersErrorEnum.NOTIFYSUCCESSLOG_NOT_FOUND);
            return false;
        }
        if (params.doubleValue() < 0.001 || params.subtract(refund.getMoney()).doubleValue() > 0.001) {
            result.setMetaCode(RefundErrorEnum.REFUND_MONEY_ERROR);
            return false;
        }
        if (params.subtract(refund.getMoney()).doubleValue() < 0) {
            log.info("change refund value, old=" + refund.getMoney().doubleValue() + ",new=" + params.doubleValue());
            refund.setMoney(params);
        }
        refund.setPaytype(payProvider.getPayType());
        refund.setDirect(refund.getMoney());
        refund.setTradeno(notifySuccessLogModel.getTradeno());
        refund.setOutno(RefundModel.genOutno(refund));
        refund.setStatus(RefundModel.STATUS_WAITING);
        refund.setSendtime(DateUtils.unix_timestamp());

        String url = payProvider.refund(refund);
        if (url.equals(BasePayProvider.FAIL_CODE)) {
            result.setMetaCode(CommonMetaCode.Error);
            return false;
        }
        refundDAO.refund2third(refund);
        notifySuccessLogModel.setRefund2third(notifySuccessLogModel.getRefund2third().add(refund.getMoney()));
        notifySuccessLogModel.setModtime(DateUtils.unix_timestamp());
        notifySuccessLogDAO.refund2third(notifySuccessLogModel);
        result.setData(url);
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, BigDecimal params) {
        RefundLogModel refundlog = new RefundLogModel();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(getAction());
        refundlog.setOpid(0);
        refundlog.setComment("");
        refundlog.setAddtime(DateUtils.unix_timestamp());
        refundLogDAO.add(refundlog);
        return true;
    }
}
