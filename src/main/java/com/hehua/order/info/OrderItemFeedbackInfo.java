package com.hehua.order.info;

import com.hehua.order.info.ItemInfo;

/**
 * Created by liuweiwei on 14-8-21.
 */
public class OrderItemFeedbackInfo {

    private int time;
    private String comment;
    private ItemInfo itemInfo;
    private boolean canFeedback;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public boolean isCanFeedback() {
        return canFeedback;
    }

    public void setCanFeedback(boolean canFeedback) {
        this.canFeedback = canFeedback;
    }
}
