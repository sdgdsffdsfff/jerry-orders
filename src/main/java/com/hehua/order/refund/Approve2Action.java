package com.hehua.order.refund;

import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.RefundDAO;
import com.hehua.order.dao.RefundLogDAO;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.model.RefundLogModel;
import com.hehua.order.model.RefundModel;
import com.hehua.order.refund.params.ResultParam;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-9-24.
 * 收货成功动作
 */
@Named
public class Approve2Action extends RefundBase<Long, ResultParam> {

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Override
    public int getAction() {
        return this.ACTION_APPROVE2;
    }

    @Override
    public boolean beforeExecute(RefundModel refund, Long params, ResultParam result) {
        if (refund == null) {
            result.setMetaCode(RefundErrorEnum.PARAM_ERROR);
            return false;
        }
        if (!refund.isStatusApprove1()) {
            result.setMetaCode(RefundErrorEnum.REFUND_APPROVE2_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, Long params, ResultParam result) {
        refundDAO.updateStatus(RefundModel.STATUS_APPROVE2, refund.getId());
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, Long params) {
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
