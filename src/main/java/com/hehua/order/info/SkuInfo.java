package com.hehua.order.info;

/**
 * Created by liuweiwei on 14-8-6.
 * 下单sku信息
 */
public class SkuInfo {

    private long skuid;
    private int quantity;

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
