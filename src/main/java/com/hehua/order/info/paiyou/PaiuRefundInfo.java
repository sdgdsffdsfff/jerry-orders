package com.hehua.order.info.paiyou;

/**
 * Created by liuweiwei on 14-8-26.
 */
public class PaiuRefundInfo {
    private String orderCode;
    private String type;
    private String barCode;
    private String expressNO;
    private String reason;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getExpressNO() {
        return expressNO;
    }

    public void setExpressNO(String expressNO) {
        this.expressNO = expressNO;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
