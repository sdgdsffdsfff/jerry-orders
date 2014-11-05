package com.hehua.order.service.params;

/**
 * Created by liuweiwei on 14-8-11.
 */
public class RefundServiceApplyParam {
    private long orderid;
    private String deliveryComPinyin;
    private String deliveryNum;
    private String comment;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getDeliveryComPinyin() {
        return deliveryComPinyin;
    }

    public void setDeliveryComPinyin(String deliveryComPinyin) {
        this.deliveryComPinyin = deliveryComPinyin;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
