package com.hehua.order.info;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-7.
 * 购物车单个商品信息
 */
public class CartsInfo {

    private long id;
    public long skuid;
    private String tid;
    private int quantity;
    public long itemid;
    public String title;
    public BigDecimal price;
    private String image;
    private int limitPerUser;
    private int remainAmount;
    private boolean canBuy;
    /* 商品类型 */
    public List<ItemTypeInfo> types;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLimitPerUser() {
        return limitPerUser;
    }

    public void setLimitPerUser(int limitPerUser) {
        this.limitPerUser = limitPerUser;
    }

    public int getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(int remainAmount) {
        this.remainAmount = remainAmount;
    }

    public boolean isCanBuy() {
        return canBuy;
    }

    public void setCanBuy(boolean canBuy) {
        this.canBuy = canBuy;
    }

    public List<ItemTypeInfo> getTypes() {
        return types;
    }

    public void setTypes(List<ItemTypeInfo> types) {
        this.types = types;
    }
}
