package com.hehua.order.refund;

import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.dao.RefundDAO;
import com.hehua.order.dao.RefundLogDAO;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.model.RefundLogModel;
import com.hehua.order.model.RefundModel;
import com.hehua.order.refund.params.ResultParam;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-9-24.
 * 取消退款动作
 */
@Named
public class CancelAction extends RefundBase<Long, ResultParam> {

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Override
    public int getAction() {
        return this.ACTION_CANCEL;
    }

    @Override
    public boolean beforeExecute(RefundModel refund, Long userid, ResultParam result) {
        if (refund == null || refund.getUserid() != userid) {
            result.setMetaCode(RefundErrorEnum.REFUND_NOT_YOURS);
            return false;
        }
        if (!refund.canCancel()) {
            result.setMetaCode(RefundErrorEnum.REFUND_CANNOT_CANCEL);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, Long userid, ResultParam result) {
        refundDAO.updateStatus(RefundModel.STATUS_CANCEL, refund.getId());
        ordersDAO.updateStatus(refund.getOrderid(), OrdersModel.STATUS_SIGNED);
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, Long userid) {
        RefundLogModel refundlog = new RefundLogModel();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(getAction());
        refundlog.setOpid(userid.intValue());
        refundlog.setComment("");
        refundlog.setAddtime(DateUtils.unix_timestamp());
        refundLogDAO.add(refundlog);
        return true;
    }
}
