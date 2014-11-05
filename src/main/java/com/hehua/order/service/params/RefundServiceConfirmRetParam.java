package com.hehua.order.service.params;

import com.hehua.order.info.*;

import java.util.List;

/**
 * Created by liuweiwei on 14-9-23.
 * 退款确认返回参数
 */
public class RefundServiceConfirmRetParam {

    private List<RefundItemInfo> items;

    private List<SkuInfo> selectedSkus;

    private double canRefundMoney;

    private List<RefundReasonInfo> refundReasons;

    private UserInfo userInfo;

    public List<RefundItemInfo> getItems() {
        return items;
    }

    public void setItems(List<RefundItemInfo> items) {
        this.items = items;
    }

    public List<SkuInfo> getSelectedSkus() {
        return selectedSkus;
    }

    public void setSelectedSkus(List<SkuInfo> selectedSkus) {
        this.selectedSkus = selectedSkus;
    }

    public double getCanRefundMoney() {
        return canRefundMoney;
    }

    public void setCanRefundMoney(double canRefundMoney) {
        this.canRefundMoney = canRefundMoney;
    }

    public List<RefundReasonInfo> getRefundReasons() {
        return refundReasons;
    }

    public void setRefundReasons(List<RefundReasonInfo> refundReasons) {
        this.refundReasons = refundReasons;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
