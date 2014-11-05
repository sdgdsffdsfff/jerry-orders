package com.hehua.order.model;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-8-15.
 */
public class CreditModel {

    private long userid;
    private BigDecimal value;
    private BigDecimal rebate;
    private BigDecimal giftcard;
    private BigDecimal recharge;
    private BigDecimal direct;
    private int modtime;

    public void initWhenAdd(long userid) {
        this.userid = userid;
        BigDecimal zero = new BigDecimal(0);
        this.value = zero;
        this.rebate = zero;
        this.giftcard = zero;
        this.recharge = zero;
        this.direct = zero;
        this.modtime = (int)(System.currentTimeMillis()/1000L);
    }
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getGiftcard() {
        return giftcard;
    }

    public void setGiftcard(BigDecimal giftcard) {
        this.giftcard = giftcard;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getDirect() {
        return direct;
    }

    public void setDirect(BigDecimal direct) {
        this.direct = direct;
    }

    public int getModtime() {
        return modtime;
    }

    public void setModtime(int modtime) {
        this.modtime = modtime;
    }
}
