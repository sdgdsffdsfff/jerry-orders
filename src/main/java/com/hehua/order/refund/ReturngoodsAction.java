package com.hehua.order.refund;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.DeliveryCompanyDAO;
import com.hehua.order.dao.RefundDAO;
import com.hehua.order.dao.RefundLogDAO;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.model.RefundLogModel;
import com.hehua.order.model.RefundModel;
import com.hehua.order.refund.params.ResultParam;
import com.hehua.order.refund.params.ReturngoodsParam;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-9-24.
 */
@Named
public class ReturngoodsAction extends RefundBase<ReturngoodsParam, ResultParam>{

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Inject
    private DeliveryCompanyDAO deliveryCompanyDAO;

    @Override
    public int getAction() {
        return this.ACTION_RETURNGOODS;
    }

    @Override
    public boolean beforeExecute(RefundModel refund, ReturngoodsParam params, ResultParam result) {
        if (refund == null) {
            result.setMetaCode(RefundErrorEnum.REFUND_NOT_EXISTS);
            return false;
        }
        if (!refund.isStatusApprove1()) {
            result.setMetaCode(RefundErrorEnum.REFUND_STATUS_ERROR);
            return false;
        }
        if (refund.getUserid() != params.getUserid()) {
            result.setMetaCode(RefundErrorEnum.REFUND_NOT_YOURS);
            return false;
        }
        if (StringUtils.isBlank(params.getDeliveryComPinyin())) {
            result.setMetaCode(RefundErrorEnum.REFUND_DELIVERY_COMPANY_ERROR);
            return false;
        }
        if (StringUtils.isBlank(params.getDeliveryNum())) {
            result.setMetaCode(RefundErrorEnum.REFUND_DELIVERY_DELIVERYNUM_NOT_EXISTS);
            return false;
        }
        if (deliveryCompanyDAO.getByPinyin(params.getDeliveryComPinyin()) == null) {
            result.setMetaCode(RefundErrorEnum.REFUND_DELIVERY_COMPANY_NOT_SUPPORT);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(RefundModel refund, ReturngoodsParam params, ResultParam result) {
        String deliveryInfo = refund.getDeliveryinfo();
        if (StringUtils.isBlank(deliveryInfo)) {
            JSONObject jo = new JSONObject();
            jo.put("deliveryComPinyin", params.getDeliveryComPinyin());
            jo.put("deliveryNum", params.getDeliveryNum());
            refund.setDeliveryInfo(jo.toJSONString());
        } else {
            JSONObject jo = JSON.parseObject(deliveryInfo);
            jo.put("deliveryComPinyin", params.getDeliveryComPinyin());
            jo.put("deliveryNum", params.getDeliveryNum());
            refund.setDeliveryInfo(jo.toJSONString());
        }
        refundDAO.updateDeliveryInfo(refund);
        return true;
    }

    @Override
    public boolean afterExecute(RefundModel refund, ReturngoodsParam params) {
        RefundLogModel refundlog = new RefundLogModel();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(getAction());
        refundlog.setOpid(0);
        refundlog.setComment("物流公司：" + deliveryCompanyDAO.getByPinyin(params.getDeliveryComPinyin()).getName() + "\n" + "物流单号：" + params.getDeliveryNum());
        refundlog.setAddtime(DateUtils.unix_timestamp());
        refundLogDAO.add(refundlog);
        return true;
    }
}
