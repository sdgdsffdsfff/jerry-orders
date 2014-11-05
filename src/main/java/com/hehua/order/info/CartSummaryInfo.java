package com.hehua.order.info;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-22.
 * 用户购物车概要信息
 */
public class CartSummaryInfo {
    /* 购物车商品总金额 */
    private BigDecimal totalFee;
    /* 邮费提示文案 */
    private String deliveryFeeTips;
    /* 购物车商品信息 */
    private List<CartsInfo> carts;

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public String getDeliveryFeeTips() {
        return deliveryFeeTips;
    }

    public void setDeliveryFeeTips(String deliveryFeeTips) {
        this.deliveryFeeTips = deliveryFeeTips;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public List<CartsInfo> getCarts() {
        return carts;
    }

    public void setCarts(List<CartsInfo> carts) {
        this.carts = carts;
    }
}
