package com.hehua.order.model;

import java.util.HashMap;

/**
 * Created by liuweiwei on 14-8-4.
 */
public class DeliveryInfoModel {

    private long orderid;
    private String address;
    private int invoicetype;
    private String invoicecomment;
    private String deliverycomment;

    public static final int TYPE_INVOICE_NONE = 0;
    public static final int TYPE_INVOICE_PERSON = 1;
    public static final int TYPE_INVOICE_COMPANY = 2;


    public static HashMap<Integer, String> invoiceDesc = new HashMap<Integer, String>() {
        {
            put(0, "不开发票");
            put(1, "个人");
            put(2, "公司");
        }
    };

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getInvoicetype() {
        return invoicetype;
    }

    public void setInvoicetype(int invoicetype) {
        this.invoicetype = invoicetype;
    }

    public String getInvoicecomment() {
        return invoicecomment;
    }

    public void setInvoicecomment(String invoicecomment) {
        this.invoicecomment = invoicecomment;
    }

    public String getDeliverycomment() {
        return deliverycomment;
    }

    public void setDeliverycomment(String deliverycomment) {
        this.deliverycomment = deliverycomment;
    }

    @Override
    public String toString() {
        return "DeliveryInfoModel{" +
                "orderid=" + orderid +
                ", address='" + address + '\'' +
                ", invoicetype=" + invoicetype +
                ", invoicecomment='" + invoicecomment + '\'' +
                ", deliverycomment='" + deliverycomment + '\'' +
                '}';
    }
}
