package com.hehua.order.info;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Created by liuweiwei on 14-8-7.
 */
@JSONType(ignores = { "status" })
public class DeliveryInfo {

    private String deliveryCompany;

    private String deliveryNum;

    private String deliveryComPinyin;

    private List<TraceInfo> traceInfo;

    private int status;

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public String getDeliveryComPinyin() {
        return deliveryComPinyin;
    }

    public void setDeliveryComPinyin(String deliveryComPinyin) {
        this.deliveryComPinyin = deliveryComPinyin;
    }

    public List<TraceInfo> getTraceInfo() {
        return traceInfo;
    }

    public void setTraceInfo(List<TraceInfo> traceInfo) {
        this.traceInfo = traceInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
