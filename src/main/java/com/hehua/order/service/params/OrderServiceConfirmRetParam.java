package com.hehua.order.service.params;

import com.hehua.order.info.AddressInfo;
import com.hehua.order.info.SkuInfo;
import com.hehua.order.info.PayTypeInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-7.
 */
public class OrderServiceConfirmRetParam {

    private List<SkuInfo> items;
    private BigDecimal totalFee;
    private BigDecimal orderFee;
    private BigDecimal deliveryFee;
    private int defaultPayType;
    private List<PayTypeInfo> payTypes;
    private AddressInfo defaultAddress;
    private int invoiceType;
    private String invoiceComment;
    private String tips;

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(BigDecimal orderFee) {
        this.orderFee = orderFee;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public int getDefaultPayType() {
        return defaultPayType;
    }

    public void setDefaultPayType(int defaultPayType) {
        this.defaultPayType = defaultPayType;
    }

    public List<PayTypeInfo> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<PayTypeInfo> payTypes) {
        this.payTypes = payTypes;
    }

    public AddressInfo getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(AddressInfo defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public List<SkuInfo> getItems() {
        return items;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceComment() {
        return invoiceComment;
    }

    public void setInvoiceComment(String invoiceComment) {
        this.invoiceComment = invoiceComment;
    }

    public void setItems(List<SkuInfo> items) {
        this.items = items;
    }
    
    public String getTips() {
        return tips;
    }
    
    public void setTips(String tips) {
        this.tips = tips;
    }
    
}
