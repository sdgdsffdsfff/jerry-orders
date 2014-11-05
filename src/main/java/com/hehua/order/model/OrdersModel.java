package com.hehua.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuweiwei on 14-7-23.
 */
public class OrdersModel {

    private long id;
    private long userid;
    private BigDecimal totalfee;
    private BigDecimal orderfee;
    private BigDecimal deliveryfee;
    private BigDecimal rebate;
    private BigDecimal giftcard;
    private BigDecimal recharge;
    private BigDecimal direct;
    private BigDecimal payed;
    private BigDecimal reduce;
    private BigDecimal credit;
    private BigDecimal outmoney;
    private String mobile;
    private int ordertime;
    private long orderip;
    private int paytime;
    private int paytype;
    private int status;
    private int attributes;

    private List<OrderItemsModel> orderitems;

    /* 已下单 */
    public static final int STATUS_NEW = 0;
    /* 24小时未付款，已关闭 */
    public static final int STATUS_CLOSED = 31;
    /* 全额支付 */
    public static final int STATUS_PAYED = 32;
    /* 已发货 */
    public static final int STATUS_DELIVERIED = 52;
    /* 已签收 */
    public static final int STATUS_SIGNED = 53;
    /* 退款中 */
    public static final int STATUS_REFUNDING = 63;
    /* 已退款 */
    public static final int STATUS_REFUND = 64;

    public static HashMap<Integer, String> statusDesc = new HashMap<Integer, String>() {
        {
            put(0, "待付款");
            put(31, "已关闭");// 此状态下的订单对用户不可见
            put(32, "已付款");
            put(52, "已发货");
            put(53, "已签收");
            put(63, "退款中");
            put(64, "已退款");
        }
    };

    /**
     * 订单是否可支付
     * @return
     */
    public boolean canPay() {
        return this.status < this.STATUS_CLOSED;
    }

    /**
     * 是否可申请退款
     * @return
     */
    public boolean canRefund() {
        return this.status >= this.STATUS_SIGNED && this.status < this.STATUS_REFUNDING;
    }

    /**
     * 是否有退款
     * @return
     */
    public boolean hasRefund() {
        return this.status == this.STATUS_REFUNDING || this.status == this.STATUS_REFUND;
    }

    /**
     * 是否已发货
     * attributes第一位用来区分已发货退款和未发货退款
     * @return
     */
    public boolean hasDeliveried() {
        return (this.attributes & 1) == 1;
    }

    public boolean hasPayed() {
        return this.status >= this.STATUS_PAYED;
    }

    /**
     * 设置为已发货
     */
    public void setDeliveried() {
        this.status = this.STATUS_DELIVERIED;
        this.attributes = this.attributes | 1;
    }

    /**
     * 设为已签收
     */
    public void setSigned() {
        this.status = this.STATUS_SIGNED;
    }

    /**
     * attributes第二位用来标示订单是否已同步到派友
     */
    public void setSendedToPaiu() {
        this.attributes = this.attributes | 2;
    }

    /**
     * 是否已发送至paiu
     * @return
     */
    public boolean isSendedToPaiu() {
        return ((this.attributes >> 1) & 1) == 1;
    }

    /**
     * 是否为已支付
     * @return
     */
    public boolean isStatusPayed() {
        return this.status == this.STATUS_PAYED;
    }

    /**
     * 是否为已签收
     * @return
     */
    public boolean isStatusSigned() {
        return this.status == this.STATUS_SIGNED;
    }

    /**
     * 是否为已发货
     * @return
     */
    public boolean isStatusDeliveried() {
        return this.status == this.STATUS_DELIVERIED;
    }

    /**
     * 是否为退款中
     * @return
     */
    public boolean isStatusRefunding() {
        return this.status == this.STATUS_REFUNDING;
    }

    public void initWhenAdd() {
        this.setStatus(0);
        BigDecimal zero = new BigDecimal(0);
        this.setRebate(zero);
        this.setGiftcard(zero);
        this.setRecharge(zero);
        this.setDirect(zero);
        this.setPayed(zero);
        this.setReduce(zero);
        this.setCredit(zero);
        this.setOutmoney(zero);
        this.setPaytime(0);
        this.setAttributes(0);
    }
    /**
     * 生成外部唯一订单号
     * @param order
     * @param isPay 是否为支付
     * @return
     */
    public static String genOutno(OrdersModel order, int isPay) {
        int time = (int)(System.currentTimeMillis() / 1000L);
        if (isPay == 1) {
            return "b-" + order.getId() + "-" + time;
        } else {
            return "c-" + order.getId() + "-" + time;
        }
    }

    /**
     * 根据外部唯一订单号获取业务订单号
     * @param outno
     * @return
     */
    public static long getOrderIdByOutno(String outno) {
        String[] arr = outno.split("-");
        return Long.parseLong(arr[1]);
    }

    /**
     * 获取下单类型c为充值，b为支付
     * @param outno
     * @return
     */
    public static String getOrderTypeByOutno(String outno) {
        String[] arr = outno.split("-");
        return arr[0];
    }

    public Date getOrderDate() {
        return new Date(((long)this.ordertime)*1000);
    }

    public Date getPayDate() {
        return new Date(((long)this.paytime)*1000);
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
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

    public BigDecimal getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(BigDecimal totalfee) {
        this.totalfee = totalfee;
    }

    public BigDecimal getOrderfee() {
        return orderfee;
    }

    public void setOrderfee(BigDecimal orderfee) {
        this.orderfee = orderfee;
    }

    public BigDecimal getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(BigDecimal deliveryfee) {
        this.deliveryfee = deliveryfee;
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

    public BigDecimal getPayed() {
        return payed;
    }

    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getOutmoney() {
        return outmoney;
    }

    public void setOutmoney(BigDecimal outmoney) {
        this.outmoney = outmoney;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(int ordertime) {
        this.ordertime = ordertime;
    }

    public long getOrderip() {
        return orderip;
    }

    public void setOrderip(long orderip) {
        this.orderip = orderip;
    }

    public int getPaytime() {
        return paytime;
    }

    public void setPaytime(int paytime) {
        this.paytime = paytime;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItemsModel> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<OrderItemsModel> orderitems) {
        this.orderitems = orderitems;
    }
}
