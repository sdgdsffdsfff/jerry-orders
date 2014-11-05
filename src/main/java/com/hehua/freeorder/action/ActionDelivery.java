package com.hehua.freeorder.action;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.freeorder.action.params.DeliveryParam;
import com.hehua.freeorder.action.params.ResultParam;
import com.hehua.freeorder.enums.FreeOrderEnums;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.freeorder.service.FreeOrderService;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.info.DeliveryInfo;
import com.hehua.order.model.DeliveryCompanyModel;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.order.service.DeliveryService;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-10-6.
 */
@Named
public class ActionDelivery extends ActionBase<DeliveryParam, ResultParam> {

    @Inject
    private FreeOrderService freeOrderService;

    @Inject
    private DeliveryService deliveryService;

    @Override
    public int getAction() {
        return ACTION_DELIVERY;
    }

    @Override
    public boolean beforeExecute(FreeOrderModel order, DeliveryParam params, ResultParam result) {
        if (order == null || !order.isStatusApprove1()) {
            result.setMetaCode(FreeOrderEnums.FREE_ORDER_STATUS_ERROR);
            return false;
        }
        if (StringUtils.isBlank(params.getDeliveryCompPinyin()) || StringUtils.isBlank(params.getDeliveryNum())) {
            result.setMetaCode(FreeOrderEnums.PARAM_ERROR);
            return false;
        }
        DeliveryCompanyModel deliveryCompany = deliveryService.getCompanyByPinyin(params.getDeliveryCompPinyin());
        if (deliveryCompany == null) {
            result.setMetaCode(OrdersErrorEnum.DELIVERY_COMPANY_NOT_EXISTS);
            return false;
        }
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setDeliveryComPinyin(params.getDeliveryCompPinyin());
        deliveryInfo.setDeliveryNum(params.getDeliveryNum());
        order.setDeliveryInfo(JSON.toJSONString(deliveryInfo));
        return true;
    }

    @Override
    public boolean execute(FreeOrderModel order, DeliveryParam params, ResultParam result) {
        if (!deliveryService.fillDeliveryInfo(order.getId(), OrderTraceModel.TYPE_FREE, params.getDeliveryNum(), params.getDeliveryCompPinyin())) {
            result.setMetaCode(CommonMetaCode.Error);
            return false;
        }
        return true;
    }
}
