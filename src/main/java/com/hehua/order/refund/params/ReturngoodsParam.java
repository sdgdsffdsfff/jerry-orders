package com.hehua.order.refund.params;

/**
 * Created by liuweiwei on 14-9-24.
 */
public class ReturngoodsParam {

    private int refundid;
    private long userid;
    private String deliveryComPinyin;
    private String deliveryNum;
    private String comment;

    public int getRefundid() {
        return refundid;
    }

    public void setRefundid(int refundid) {
        this.refundid = refundid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
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
