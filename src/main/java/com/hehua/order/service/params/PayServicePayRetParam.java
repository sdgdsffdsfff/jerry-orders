package com.hehua.order.service.params;

/**
 * Created by liuweiwei on 14-8-6.
 */
public class PayServicePayRetParam {
    private String payUrl;
    private long orderid;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }
}
