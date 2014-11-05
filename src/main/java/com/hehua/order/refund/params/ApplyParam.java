package com.hehua.order.refund.params;

import com.hehua.order.info.SkuInfo;
import com.hehua.order.info.UserInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-9-23.
 */
public class ApplyParam {

    private long orderid;
    private long userid;
    private List<SkuInfo> skus;
    private UserInfo userInfo;
    private int reasonid;
    private String comment;

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public List<SkuInfo> getSkus() {
        return skus;
    }

    public void setSkus(List<SkuInfo> skus) {
        this.skus = skus;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getReasonid() {
        return reasonid;
    }

    public void setReasonid(int reasonid) {
        this.reasonid = reasonid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
