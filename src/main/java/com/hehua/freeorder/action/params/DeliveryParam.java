package com.hehua.freeorder.action.params;

/**
 * Created by liuweiwei on 14-10-13.
 * 设为已发货参数
 */
public class DeliveryParam {

    private String deliveryCompPinyin;
    private String deliveryNum;

    public String getDeliveryCompPinyin() {
        return deliveryCompPinyin;
    }

    public void setDeliveryCompPinyin(String deliveryCompPinyin) {
        this.deliveryCompPinyin = deliveryCompPinyin;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }
}
