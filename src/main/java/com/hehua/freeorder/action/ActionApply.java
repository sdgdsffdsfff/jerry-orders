package com.hehua.freeorder.action;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.freeorder.action.params.ApplyParam;
import com.hehua.freeorder.action.params.ResultParam;
import com.hehua.freeorder.enums.FreeOrderEnums;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.item.domain.FreeFlash;
import com.hehua.item.service.FreeFlashService;
import com.hehua.order.service.DeliveryService;
import com.hehua.user.domain.Address;
import com.hehua.user.service.AddressService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-10-6.
 * 申请众测动作
 */
@Named
public class ActionApply extends ActionBase<ApplyParam, ResultParam> {

    @Inject
    private AddressService addressService;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private FreeFlashService freeFlashService;

    @Override
    public int getAction() {
        return ACTION_APPLY;
    }

    @Override
    public boolean beforeExecute(FreeOrderModel order, ApplyParam params, ResultParam result) {
        if (!freeOrderService.canApply(params.getUserid())) {
            result.setMetaCode(FreeOrderEnums.ORDER_NOT_FINISHED);
            return false;
        }
        Address address = addressService.getAddressById(params.getAddressid());
        if (address == null) {
            result.setMetaCode(FreeOrderEnums.ADDRESS_NOT_EXIST);
            return false;
        }
        if (address.getUid() != params.getUserid()) {
            result.setMetaCode(FreeOrderEnums.ADDRESS_NOT_YOURS);
            return false;
        }
        order.setAddressInfo(JSON.toJSONString(deliveryService.getAddressInfo(address)));

        FreeFlash flash = freeFlashService.getFreeFlash(params.getFreeFlashid());
        if (!freeFlashService.canApply(flash)) {
            result.setMetaCode(FreeOrderEnums.FREE_FLASH_SOLDOUT);
            return false;
        }
        System.out.println(flash.getItemid() + "\t" + flash.getSkuid());
        order.setFreeFlashId(params.getFreeFlashid());
        order.setItemid(flash.getItemid());
        order.setSkuid(flash.getSkuid());
        return true;
    }

    @Override
    public boolean execute(FreeOrderModel order, ApplyParam params, ResultParam result) {
        order.setUserid(params.getUserid());
        order.setApplyip(params.getApplyip());
        freeOrderDAO.add(order);

        if (!freeFlashService.updateApplyNum(order.getFreeFlashId())) {
            result.setMetaCode(CommonMetaCode.Error);
            return false;
        }
        return true;
    }
}
