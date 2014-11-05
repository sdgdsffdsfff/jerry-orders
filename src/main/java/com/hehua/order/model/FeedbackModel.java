package com.hehua.order.model;

import java.util.HashMap;

/**
 * Created by liuweiwei on 14-8-18.
 */
public class FeedbackModel {

    private int id;
    private long userid;
    private long itemid;
    private long orderid;
    private long skuid;
    private int score;
    private String comment;
    private int status;
    private int attributes;
    private int addtime;
    private int modtime;

    public static final HashMap<Integer, String> descScore = new HashMap<Integer, String>() {
        {
            put(1, "差评");
            put(2, "差评");
            put(3, "中评");
            put(4, "好评");
            put(5, "好评");
        }
    };

    public static final int FEEDBACK_APPEND = 1;

    public void initWhenAdd() {
        int time = (int)(System.currentTimeMillis()/1000L);
        this.addtime = time;
        this.modtime = time;
        this.status = 0;
        this.attributes = 0;
        this.score = 0;
    }

    /**
     * 是否为追加评论
     * attributes第一位用来区分是否为追加评论
     * @param flag
     */
    public void setAppend(int flag) {
        this.attributes |= flag;
    }

    public boolean isAppend() {
        return (this.attributes & 1) == 1;
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

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
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
