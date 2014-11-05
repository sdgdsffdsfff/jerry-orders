package com.hehua.order.model;

/**
 * Created by liuweiwei on 14-7-27.
 */
public class CartModel {
    private long id;
    private long userid;
    private long itemid;
    private long skuid;
    private int quantity;
    private String tid;
    private int addtime;
    private int modtime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getItemid() {
        return itemid;
    }
    
    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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
