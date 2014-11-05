package com.hehua.order.service.params;

import com.hehua.order.info.SkuInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-9-23.
 * 退款确认请求参数
 */
public class RefundServiceConfirmParam {

    private long userid;

    private long orderid;

    private List<SkuInfo> skus;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public List<SkuInfo> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuInfo> skus) {
        this.skus = skus;
    }
}
