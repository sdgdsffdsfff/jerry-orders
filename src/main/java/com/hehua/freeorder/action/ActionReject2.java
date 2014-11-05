package com.hehua.freeorder.action;

import com.hehua.freeorder.action.params.ResultParam;
import com.hehua.freeorder.enums.FreeOrderEnums;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.freeorder.service.FreeOrderService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by liuweiwei on 14-10-7.
 */
@Named
public class ActionReject2 extends ActionBase<String, ResultParam> {

    @Inject
    private FreeOrderService freeOrderService;

    @Override
    public int getAction() {
        return ACTION_REJECT2;
    }

    @Override
    public boolean beforeExecute(FreeOrderModel order, String params, ResultParam result) {
        if (order == null || !order.isStatusComplete()) {
            result.setMetaCode(FreeOrderEnums.FREE_ORDER_STATUS_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(FreeOrderModel order, String params, ResultParam result) {
        if (!freeOrderService.setReject2(order)) {
            return false;
        }
        return true;
    }
}
