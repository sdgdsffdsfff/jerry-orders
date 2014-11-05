package com.hehua.order.info;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.Date;

/**
 * Created by liuweiwei on 14-8-7.
 */
@JSONType(ignores = {"timestampDate"})
public class RefundEventInfo {
    private int timestamp;
    private int type;
    private String typeName;
    private String message;
    private String comment;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestampDate() {
        return new Date(((long)this.timestamp)*1000);
    }
 }
