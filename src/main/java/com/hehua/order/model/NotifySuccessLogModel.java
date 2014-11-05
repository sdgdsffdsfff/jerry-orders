package com.hehua.order.model;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-8-14.
 */
public class NotifySuccessLogModel {

    private long id;
    private long userid;
    private long orderid;
    private String outno;
    private int type;
    private int paytype;
    private BigDecimal money;
    private String tradeno;
    private String buyer;
    private int paytime;
    private int addtime;
    private int modtime;
    private String data;
    private BigDecimal refund2third;
    private BigDecimal refund2credit;

    public void initWhenAdd(NotifyModel notifyModel) {
        if (notifyModel.getType() == NotifyModel.TYPE_CHARGE) {
            this.userid = notifyModel.getObjid();
        } else {
            this.orderid = notifyModel.getObjid();
        }
        this.outno = notifyModel.getOutno();
        this.type = notifyModel.getType();
        this.paytype = notifyModel.getPaytype();
        this.money = notifyModel.getMoney();
        this.tradeno = notifyModel.getTradeno();
        this.buyer = notifyModel.getBuyer();
        this.paytime = notifyModel.getPaytime();
        this.data = notifyModel.getData();
        BigDecimal zero = new BigDecimal(0);
        this.refund2third = zero;
        this.refund2credit = zero;
        int now = (int)(System.currentTimeMillis()/1000L);
        this.addtime = now;
        this.modtime = now;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getOutno() {
        return outno;
    }

    public void setOutno(String outno) {
        this.outno = outno;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getPaytime() {
        return paytime;
    }

    public void setPaytime(int paytime) {
        this.paytime = paytime;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getRefund2third() {
        return refund2third;
    }

    public void setRefund2third(BigDecimal refund2third) {
        this.refund2third = refund2third;
    }

    public BigDecimal getRefund2credit() {
        return refund2credit;
    }

    public void setRefund2credit(BigDecimal refund2credit) {
        this.refund2credit = refund2credit;
    }
}
