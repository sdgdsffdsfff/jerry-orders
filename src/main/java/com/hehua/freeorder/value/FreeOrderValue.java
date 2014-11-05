package com.hehua.freeorder.value;

import com.hehua.freeorder.info.FreeOrderInfo;
import com.hehua.item.domain.Item;
import com.hehua.item.domain.ItemAppraise;
import com.hehua.user.domain.User;

/**
 * Date 14-10-15.
 * Author WangJun
 * Email wangjuntytl@163.com
 */
public class FreeOrderValue extends FreeOrderInfo {

    private FreeOrderInfo freeOrderInfo;

    private ItemAppraise itemAppraise;

    private User user;

    private Item item;

    public FreeOrderInfo getFreeOrderInfo() {
        return freeOrderInfo;
    }

    public void setFreeOrderInfo(FreeOrderInfo freeOrderInfo) {
        this.freeOrderInfo = freeOrderInfo;
    }

    public ItemAppraise getItemAppraise() {
        return itemAppraise;
    }

    public void setItemAppraise(ItemAppraise itemAppraise) {
        this.itemAppraise = itemAppraise;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
