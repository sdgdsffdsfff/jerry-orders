package com.hehua.order.service.params;

import com.hehua.order.info.SkuInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-8-7.
 */
public class OrderServiceConfirmParam {

    private long userid;
    private List<SkuInfo> items;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public List<SkuInfo> getItems() {
        return items;
    }

    public void setItems(List<SkuInfo> items) {
        this.items = items;
    }
}
