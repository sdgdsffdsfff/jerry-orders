package com.hehua.order.model;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-9-22.
 */
public class RefundItemsModel {

    private int id;
    private int refundid;
    private long userid;
    private long orderid;
    private long itemid;
    private long skuid;
    private int quantity;
    private BigDecimal money;
    private int status;

    public static final int STATUS_INIT = 0;
    public static final int STATUS_SUCCESS = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
