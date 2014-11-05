package com.hehua.order.info;

/**
 * Created by liuweiwei on 14-8-21.
 * 新增评价信息
 */
public class FeedbackInfo {

    private long orderid;
    private long skuid;
    private String comment;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }
}
