package com.hehua.order.info.paiyou;

import java.util.List;

/**
 * Created by liuweiwei on 14-8-25.
 */
public class OrderInfo {

    private String orderCode;
    private String createTime;
    private String orderStatus;
    private String consigneeUser;
    private String consigneeZip;
    private String consigneePhone;
    private String consigneeMobile;
    private String consigneeProvince;
    private String consigneeProvinceCo; //可选
    private String consigneeCity;
    private String consigneeCityCo; //可选
    private String consigneeDistrict;
    private String consigneeDistrictCo; //可选
    private String consigneeAddress;
    private double payment;
    private String buyerMemo; //可选
    private String sellerMemo; //可选
    private int totalNum;
    private String payType;
    private String hasInvoice;
    private List<GoodsInfo> goodsInfo;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getConsigneeUser() {
        return consigneeUser;
    }

    public void setConsigneeUser(String consigneeUser) {
        this.consigneeUser = consigneeUser;
    }

    public String getConsigneeZip() {
        return consigneeZip;
    }

    public void setConsigneeZip(String consigneeZip) {
        this.consigneeZip = consigneeZip;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }

    public String getConsigneeProvince() {
        return consigneeProvince;
    }

    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince;
    }

    public String getConsigneeProvinceCo() {
        return consigneeProvinceCo;
    }

    public void setConsigneeProvinceCo(String consigneeProvinceCo) {
        this.consigneeProvinceCo = consigneeProvinceCo;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsigneeCityCo() {
        return consigneeCityCo;
    }

    public void setConsigneeCityCo(String consigneeCityCo) {
        this.consigneeCityCo = consigneeCityCo;
    }

    public String getConsigneeDistrict() {
        return consigneeDistrict;
    }

    public void setConsigneeDistrict(String consigneeDistrict) {
        this.consigneeDistrict = consigneeDistrict;
    }

    public String getConsigneeDistrictCo() {
        return consigneeDistrictCo;
    }

    public void setConsigneeDistrictCo(String consigneeDistrictCo) {
        this.consigneeDistrictCo = consigneeDistrictCo;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public String getSellerMemo() {
        return sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(String hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public List<GoodsInfo> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodInfo(List<GoodsInfo> goodInfo) {
        this.goodsInfo = goodInfo;
    }
}
