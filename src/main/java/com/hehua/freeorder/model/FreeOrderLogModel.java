package com.hehua.freeorder.model;

/**
 * Created by liuweiwei on 14-10-7.
 */
public class FreeOrderLogModel {

    private int id;
    private int freeOrderId;
    private int opid;
    private int type;
    private String comment;
    private int addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeOrderId() {
        return freeOrderId;
    }

    public void setFreeOrderId(int freeOrderId) {
        this.freeOrderId = freeOrderId;
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
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
