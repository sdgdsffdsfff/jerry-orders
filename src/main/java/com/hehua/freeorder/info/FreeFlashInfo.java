package com.hehua.freeorder.info;

import com.hehua.order.info.ItemTypeInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-10-11.
 */
public class FreeFlashInfo {
    private int freeFlashid;
    private int applyNum;
    private int totalNum;
    private String image;
    private long itemid;
    private double price;
    private long skuid;
    private String title;
    private List<ItemTypeInfo> types;

    public int getFreeFlashid() {
        return freeFlashid;
    }

    public void setFreeFlashid(int freeFlashid) {
        this.freeFlashid = freeFlashid;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemTypeInfo> getTypes() {
        return types;
    }

    public void setTypes(List<ItemTypeInfo> types) {
        this.types = types;
    }
}
