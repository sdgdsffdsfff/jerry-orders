package com.hehua.order.info;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-7.
 * 单个订单信息
 */
public class OrderInfo {

    private long orderid;
    private int status;
    private BigDecimal totalFee;
    private BigDecimal orderFee;
    private BigDecimal deliveryFee;
    private int payType;
    private int invoiceType;
    private String invoiceComment;
    private int orderTime;
    private int payTime;
    private List<ItemInfo> itemInfo;
    private DeliveryInfo deliveryInfo;
    private AddressInfo address;
    private List<RefundInfo> refundInfo;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
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

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public int getPayTime() {
        return payTime;
    }

    public void setPayTime(int payTime) {
        this.payTime = payTime;
    }

    public List<ItemInfo> getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(List<ItemInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }

    public List<RefundInfo> getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(List<RefundInfo> refundInfo) {
        this.refundInfo = refundInfo;
    }
}
