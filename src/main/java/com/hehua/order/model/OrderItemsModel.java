package com.hehua.order.model;

import com.google.common.base.Function;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-7-26.
 */
public class OrderItemsModel {

    private long userid;
    private long orderid;
    private long itemid;
    private long skuid;
    private int quantity;
    private BigDecimal buyprice;
    private BigDecimal saleprice;
    
    public long getUserid() {
        return userid;
    }
    
    public void setUserid(long userid) {
        this.userid = userid;
    }

    public static Function<OrderItemsModel, Integer> itemIdExtractor = new Function<OrderItemsModel, Integer>() {

        @Override
        public Integer apply(OrderItemsModel input) {

            return (int)input.getItemid();
        }
    };

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

    public BigDecimal getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(BigDecimal buyprice) {
        this.buyprice = buyprice;
    }

    public BigDecimal getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(BigDecimal saleprice) {
        this.saleprice = saleprice;
    }

    @Override
    public String toString() {
        return "OrderItemsModel{" +
                "orderid=" + orderid +
                ", itemid=" + itemid +
                ", skuid=" + skuid +
                ", quantity=" + quantity +
                ", buyprice=" + buyprice +
                ", saleprice=" + saleprice +
                '}';
    }
}
