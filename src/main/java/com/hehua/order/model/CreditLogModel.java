package com.hehua.order.model;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by liuweiwei on 14-8-15.
 */
public class CreditLogModel {

    private int id;
    private long userid;
    private int type;
    private long objid;
    private BigDecimal value;
    private BigDecimal rebate;
    private BigDecimal giftcard;
    private BigDecimal recharge;
    private BigDecimal direct;
    private BigDecimal balance;
    private int addtime;

    /* 支付 */
    public static final int TYPE_PAY = 0;
    /* 充值 */
    public static final int TYPE_CHARGE = 1;
    /* 购买 */
    public static final int TYPE_BUY = 2;

    public static final HashMap<Integer, String> descType = new HashMap<Integer, String>() {
        {
            put(0, "支付");
            put(1, "充值");
            put(2, "购买");
        }
    };

    public void initWhenPay(BigDecimal value, long objid, long userid) {

        this.userid = userid;
        this.type = CreditLogModel.TYPE_PAY;
        this.objid = objid;
        this.value = value;
        this.direct = value;

        BigDecimal zero = new BigDecimal(0);
        this.rebate = zero;
        this.giftcard = zero;
        this.recharge = zero;
        this.addtime = (int)(System.currentTimeMillis()/1000L);
    }

    public void initWhenBuy(long userid, long objid, BigDecimal value, BigDecimal direct, BigDecimal recharge, BigDecimal giftcard, BigDecimal rebate) {
        this.userid = userid;
        this.objid = objid;
        this.value = value;
        this.direct = direct;
        this.recharge = recharge;
        this.giftcard = giftcard;
        this.rebate = rebate;
        this.addtime = (int)(System.currentTimeMillis()/1000L);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getObjid() {
        return objid;
    }

    public void setObjid(long objid) {
        this.objid = objid;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "CreditLogModel{" +
                "id=" + id +
                ", userid=" + userid +
                ", type=" + type +
                ", objid=" + objid +
                ", value=" + value +
                ", rebate=" + rebate +
                ", giftcard=" + giftcard +
                ", recharge=" + recharge +
                ", direct=" + direct +
                ", balance=" + balance +
                ", addtime=" + addtime +
                '}';
    }
}
