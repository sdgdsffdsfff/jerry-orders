package com.hehua.order.service.params;

import com.hehua.order.info.SkuInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-7-29.
 */
public class OrdersServiceCreateOrderParam {

    private long userid;
    private String ip;
    private String mobile;
    private long addressid;
    private int payType;
    private int invoiceType;
    private String invoiceComment;
    private String deliveryComment;
    private List<SkuInfo> items;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getAddressid() {
        return addressid;
    }

    public void setAddressid(long addressid) {
        this.addressid = addressid;
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

    public String getDeliveryComment() {
        return deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }

    public List<SkuInfo> getItems() {
        return items;
    }

    public void setItems(List<SkuInfo> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrdersServiceCreateOrderParam{" +
                "ip='" + ip + '\'' +
                ", mobile='" + mobile + '\'' +
                ", addressid=" + addressid +
                ", payType=" + payType +
                ", invoiceType=" + invoiceType +
                ", invoiceComment='" + invoiceComment + '\'' +
                ", deliveryComment='" + deliveryComment + '\'' +
                ", items=" + items +
                '}';
    }
}
