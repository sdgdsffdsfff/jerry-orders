package com.hehua.order.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by liuweiwei on 14-8-9.
 */
public class RefundModel {

    private int id;
    private long userid;
    private long orderid;
    private int paytype;
    private BigDecimal money;
    private BigDecimal recharge;
    private BigDecimal direct;
    private String tradeno;
    private String outno;
    private int type;
    private int method;
    private int reason;
    private int status;
    private String deliveryinfo;
    private String comment;
    private int sendtime;
    private int succtime;
    private int failtime;
    private int addtime;
    private int modtime;
    private int attributes;


    /* 新增退款 */
    public static final int STATUS_ADD = 0;
    /* 审核通过 */
    public static final int STATUS_APPROVE1 = 2;
    /* 审核拒绝 */
    public static final int STATUS_REJECT1 = 4;
    /* 退款取消 */
    public static final int STATUS_CANCEL = 6;
    /* 退货成功 */
    public static final int STATUS_APPROVE2 = 8;
    /* 退货失败*/
    public static final int STATUS_REJECT2 = 10;
    /* 已付款，待结果 */
    public static final int STATUS_WAITING = 12;
    /* 退款成功 */
    public static final int STATUS_SUCCESS = 14;
    /* 退款失败 */
    public static final int STATUS_FAIL = 16;


    /* 默认类型 */
    public static final int TYPE_DEFAULT = 0;
    /* 重复支付退款 */
    public static final int TYPE_REPEAT = 1;
    /* 买光退 */
    public static final int TYPE_LESS = 2;

    public static HashMap<Integer, String> descStatus = new HashMap<Integer, String>() {
        {
            put(0, "新申请");
            put(2, "审核通过");
            put(4, "审核未通过");
            put(6, "已撤销申请");
            put(8, "退货成功");
            put(10, "退货失败");
            put(12, "退款中");
            put(14, "退款成功");
            put(16, "退款失败");
        }
    };

    public static HashMap<Integer, String> descType = new HashMap<Integer, String>() {
        {
            put(0, "默认");
            put(1, "重复支付退款");
            put(2, "买光退");
        }
    };

    public void initWhenAdd() {
        this.paytype = 0;
        this.tradeno = "";
        this.outno = "";
        this.type = RefundModel.TYPE_DEFAULT;
        this.method = 0;
        this.reason = 0;
        this.status = 0;
        this.sendtime = 0;
        this.succtime = 0;
        this.failtime = 0;
        this.addtime = (int)(System.currentTimeMillis() / 1000L);
        this.modtime = this.addtime;
        this.attributes = 0;
    }

    /**
     * 设置退款信息已发送至paiu
     */
    public void setSended2Paiu() {
        this.attributes |= 1;
    }

    public boolean isStatusNew() {
        return this.status == STATUS_ADD;
    }

    public boolean isStatusApprove1() {
        return this.status == STATUS_APPROVE1;
    }

    public boolean isStatusApprove2() {
        return this.status == STATUS_APPROVE2;
    }

    public boolean canCancel() {
        return this.status == STATUS_ADD || this.status == STATUS_APPROVE1;
    }

    public boolean canWithdraw() {
        return this.status == STATUS_APPROVE2 || this.status == STATUS_WAITING;
    }

    /**
     * 生成支付宝退款批次号
     * @param refund
     * @return
     */
    public static String genOutno(RefundModel refund) {
        //支付宝退款批次号格式：退款日期（8位）+ 3~24位流水号
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String outno = dateFormat.format(date);
        if (String.valueOf(refund.getId()).length() == 1) {
            outno += "000" + refund.getId();
        } else if (String.valueOf(refund.getId()).length() == 2) {
            outno += "00" + refund.getId();
        } else {
            outno += refund.getId();
        }
        return outno;
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

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
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

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
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

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSendtime() {
        return sendtime;
    }

    public void setSendtime(int sendtime) {
        this.sendtime = sendtime;
    }

    public int getSucctime() {
        return succtime;
    }

    public void setSucctime(int succtime) {
        this.succtime = succtime;
    }

    public int getFailtime() {
        return failtime;
    }

    public void setFailtime(int failtime) {
        this.failtime = failtime;
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

    public String getDeliveryinfo() {
        return deliveryinfo;
    }

    public void setDeliveryInfo(String deliveryinfo) {
        this.deliveryinfo = deliveryinfo;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }
}
