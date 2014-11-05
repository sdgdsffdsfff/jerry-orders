package com.hehua.freeorder.action.params;

/**
 * Created by liuweiwei on 14-10-7.
 * 申请众测请求参数
 */
public class ApplyParam {

    private int addressid;
    private long userid;
    private int freeFlashid;
    private long applyip;

    public int getAddressid() {
        return addressid;
    }

    public void setAddressid(int addressid) {
        this.addressid = addressid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getFreeFlashid() {
        return freeFlashid;
    }

    public void setFreeFlashid(int freeFlashid) {
        this.freeFlashid = freeFlashid;
    }

    public long getApplyip() {
        return applyip;
    }

    public void setApplyip(long applyip) {
        this.applyip = applyip;
    }
}
