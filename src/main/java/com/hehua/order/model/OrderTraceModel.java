package com.hehua.order.model;

import com.hehua.commons.time.DateUtils;

/**
 * Created by liuweiwei on 14-8-23.
 */
public class OrderTraceModel {

    private int id;
    private long orderid;
    private String deliveryCompanPinyin;
    private String deliveryNum;
    private int status;
    private int type;
    private String data;
    private int querytimes;
    private int lastqtime;
    private int addtime;
    private int modtime;

    public void initWhenAdd() {
        this.status = 0;
        this.data = "";
        this.querytimes = 0;
        this.lastqtime = 0;
        this.addtime = DateUtils.unix_timestamp();
        this.modtime = DateUtils.unix_timestamp();
    }

    /* 新增单号 */
    public static final int STATUS_NEW = 0;
    /* 查询存在 */
    public static final int STATUS_OK = 32;
    /* 查询已签收 */
    public static final int STATUS_SIGNED = 64;
    /* 无用已删除 */
    public static final int STATUS_DELETE = 128;

    /* 普通订单 */
    public static final int TYPE_DEFAULT = 0;
    /* 众测订单 */
    public static final int TYPE_FREE = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getDeliveryCompanPinyin() {
        return deliveryCompanPinyin;
    }

    public void setDeliveryCompanPinyin(String deliveryCompanPinyin) {
        this.deliveryCompanPinyin = deliveryCompanPinyin;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getQuerytimes() {
        return querytimes;
    }

    public void setQuerytimes(int querytimes) {
        this.querytimes = querytimes;
    }

    public int getLastqtime() {
        return lastqtime;
    }

    public void setLastqtime(int lastqtime) {
        this.lastqtime = lastqtime;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public int getModtime() {
        return modtime;
    }

    public void setModtime(int modtime) {
        this.modtime = modtime;
    }
}
