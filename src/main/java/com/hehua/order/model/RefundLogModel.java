package com.hehua.order.model;

import com.hehua.commons.time.DateUtils;

import java.util.HashMap;

/**
 * Created by liuweiwei on 14-8-9.
 */
public class RefundLogModel {

    private int id;
    private int refundid;
    private int opid;
    private int type;
    private String Comment;
    private int addtime;

    /* 新增退款 */
    public static final int TYPE_ADD = 0;
    /* 审核通过 */
    public static final int TYPE_APPROVE = 1;
    /* 审核拒绝 */
    public static final int TYPE_REJECT = 3;
    /* 已付款，待结果 */
    public static final int TYPE_WAITING = 5;
    /* 退款成功 */
    public static final int TYPE_SUCCESS = 7;
    /* 退款失败 */
    public static final int TYPE_FAIL = 9;
    /* 二审通过 */
    public static final int TYPE_APPROVE2 = 11;
    /* 二审拒绝 */
    public static final int TYPE_REJECT2 = 13;

    public static HashMap<Integer, String> descType = new HashMap<Integer, String>() {
        {
            put(0, "新申请");
            put(1, "审核通过");
            put(2, "已撤销申请");
            put(3, "审核未通过");
            put(4, "回寄商品");
            put(5, "退款中");
            put(7, "退款成功");
            put(9, "退款失败");
            put(11, "退货成功");
            put(13, "退货失败");
        }
    };

    public static HashMap<Integer, String> typeMsg = new HashMap<Integer, String>() {
        {
            put(0, "正在审核您的申请");
            put(1, "注：退货时暂不支持货到付款，请自理邮费");
            put(2, "");
            put(3, "如有疑问，请联系客服");
            put(4, "");
            put(5, "将在3~10个工作日内退回您的原支付方，无手续费");
            put(7, "已成功退款至原支付方，给您带来的麻烦我们深感抱歉");
            put(9, "如有疑问，请联系客服");
            put(11, "将在1-2个工作日内进行退款，退款金额将严格遵循荷花亲子退款规定，根据商品实际售价及退还时完整度进行审核如有疑问请联系客服");
            put(13, "如有疑问，请联系客服");
        }
    };

    public void initWhenAdd() {
        this.addtime = DateUtils.unix_timestamp();
        this.opid = 0;
        this.Comment = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefundid() {
        return refundid;
    }

    public void setRefundid(int refundid) {
        this.refundid = refundid;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
