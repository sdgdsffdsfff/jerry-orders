package com.hehua.order.model;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-8-1.
 */
public class NotifyModel {

    private long id;
    private long objid;
    private String outno;
    private int type;
    private int paytype;
    private BigDecimal money;
    private String tradeno;
    private String buyer;
    private int paytime;
    private String result;
    private String sign;
    private int status;
    private String error;
    private int addtime;
    private int modtime;
    private String data;

    public final static int TYPE_CHARGE = 1;
    public final static int TYPE_PAY = 0;

    public final static int TYPE_PAY_ALIPAYMOBILE = 1;


    public void initWhenAdd() {
        this.error = "";
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getObjid() {
        return objid;
    }

    public void setObjid(long objid) {
        this.objid = objid;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    @Override
    public String toString() {
        return "NotifyModel{" +
                "id=" + id +
                ", objid=" + objid +
                ", outno='" + outno + '\'' +
                ", type=" + type +
                ", paytype=" + paytype +
                ", money=" + money +
                ", tradeno='" + tradeno + '\'' +
                ", buyer='" + buyer + '\'' +
                ", paytime=" + paytime +
                ", result='" + result + '\'' +
                ", sign='" + sign + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", addtime=" + addtime +
                ", modtime=" + modtime +
                ", data='" + data + '\'' +
                '}';
    }
}
