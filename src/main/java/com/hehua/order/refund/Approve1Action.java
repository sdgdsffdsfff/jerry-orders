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
 * 审核通过动作
 */
@Named
public class Approve1Action extends RefundBase<String, ResultParam> {

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Override
    public int getAction() {
        return this.ACTION_APPROVE1;
    }

    @Override
    public boolean beforeExecute(RefundModel refund, String params, ResultParam result) {
        if (refund == null) {
            result.setMetaCode(RefundErrorEnum.REFUND_NOT_EXISTS);
            return false;
        }
        if (!refund.isStatusNew()) {
            result.setMetaCode(RefundErrorEnum.REFUND_APPROVE1_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, String params, ResultParam result) {
        refundDAO.updateStatus(RefundModel.STATUS_APPROVE1, refund.getId());
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, String params) {
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
