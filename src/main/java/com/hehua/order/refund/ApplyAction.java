package com.hehua.order.refund;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.Transformers;
import com.hehua.commons.time.DateUtils;
import com.hehua.order.constants.RefundReasonConfig;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.dao.RefundDAO;
import com.hehua.order.dao.RefundItemsDAO;
import com.hehua.order.dao.RefundLogDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.info.RefundItemInfo;
import com.hehua.order.info.SkuInfo;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.model.RefundItemsModel;
import com.hehua.order.model.RefundLogModel;
import com.hehua.order.model.RefundModel;
import com.hehua.order.refund.params.ApplyParam;
import com.hehua.order.refund.params.ResultParam;
import com.hehua.order.service.RefundService;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuweiwei on 14-9-22.
 * 退款申请动作
 */
@Named
public class ApplyAction extends RefundBase<ApplyParam, ResultParam> {

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Inject
    private RefundItemsDAO refundItemsDAO;

    @Inject
    private RefundService refundService;

    @Inject
    private OrdersDAO ordersDAO;

    @Override
    public boolean beforeExecute(RefundModel refund, ApplyParam param, ResultParam result) {

        if (param.getSkus() == null || param.getReasonid() == 0) {
            result.setMetaCode(RefundErrorEnum.PARAM_ERROR);
            return false;
        }
        if (RefundReasonConfig.reaseonMap.get(param.getReasonid()) == null) {
            result.setMetaCode(RefundErrorEnum.REFUND_REASON_ERROR);
            return false;
        }
        OrdersModel order = ordersDAO.getById(param.getOrderid());
        if (order == null || order.getUserid() != param.getUserid()) {
            result.setMetaCode(OrdersErrorEnum.ORDER_NOT_YOURS);
            return false;
        }
        if (!order.canRefund()) {
            result.setMetaCode(RefundErrorEnum.CAN_NOT_APPLY);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, ApplyParam param, ResultParam result) {

        List<RefundItemInfo> refundItemInfos = refundService.getRefundItemInfos(param.getOrderid());
        Map<Long, RefundItemInfo> skuIdRefundItemInfoMap = Transformers.transformAsOneToOneMap(refundItemInfos, RefundItemInfo.skuIdExtrator);

        BigDecimal totalMoney = new BigDecimal(0);
        List<RefundItemsModel> refundItemsModels = new ArrayList<>();
        for (SkuInfo skuInfo : param.getSkus()) {
            RefundItemInfo refundItemInfo = skuIdRefundItemInfoMap.get(skuInfo.getSkuid());
            if (refundItemInfo == null) {
                result.setMetaCode(RefundErrorEnum.SKU_NOT_MATCH_ORDER);
                return false;
            }
            if (refundItemInfo.getCanRefundAmount() - skuInfo.getQuantity() < 0) {
                result.setMetaCode(RefundErrorEnum.REFUND_NUM_ERROR);
                return false;
            }
            RefundItemsModel refundItem = new RefundItemsModel();
            refundItem.setUserid(param.getUserid());
            refundItem.setMoney(refundItemInfo.getPrice());
            refundItem.setQuantity(skuInfo.getQuantity());
            refundItem.setOrderid(param.getOrderid());
            refundItem.setItemid(refundItemInfo.getItemid());
            refundItem.setSkuid(refundItemInfo.getSkuid());

            refundItemsModels.add(refundItem);
            totalMoney = totalMoney.add(refundItemInfo.getPrice().multiply(new BigDecimal(skuInfo.getQuantity())));
        }

        refund.initWhenAdd();
        refund.setUserid(param.getUserid());
        refund.setOrderid(param.getOrderid());
        refund.setReason(param.getReasonid());
        refund.setMoney(totalMoney);
        refund.setComment(param.getComment() == null ? "" : param.getComment());
        if (param.getUserInfo() != null) {
            refund.setDeliveryInfo(JSON.toJSONString(param.getUserInfo()));
        }
        refundDAO.add(refund);

        for (RefundItemsModel refundItemsModel : refundItemsModels) {
            refundItemsModel.setRefundid(refund.getId());
            refundItemsDAO.add(refundItemsModel);
        }

        //设置订单为退款中
        ordersDAO.updateStatus(param.getOrderid(), OrdersModel.STATUS_REFUNDING);
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, ApplyParam param) {

        RefundLogModel refundlog = new RefundLogModel();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(getAction());
        refundlog.setOpid((int) param.getUserid());
        refundlog.setComment(StringUtils.isBlank(param.getComment()) ? "" : param.getComment());
        refundlog.setAddtime(DateUtils.unix_timestamp());
        refundLogDAO.add(refundlog);
        return true;
    }

    @Override
    public int getAction() {
        return this.ACTION_APPLY;
    }
}
