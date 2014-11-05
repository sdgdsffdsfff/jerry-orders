package com.hehua.order.refund.params;

import com.hehua.order.info.RefundItemInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-9-25.
 */
public class ItemlistParam {

    private List<RefundItemInfo> items;

    public List<RefundItemInfo> getItems() {
        return items;
    }

    public void setItems(List<RefundItemInfo> items) {
        this.items = items;
    }
}
