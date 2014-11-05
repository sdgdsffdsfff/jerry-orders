package com.hehua.order.info;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-6.
 * 单个商品信息
 */
public class ItemInfo {
    private long skuid;
    private long itemid;
    private String title;
    private BigDecimal price;
    private String image;
    private int quantity;
    private List<ItemTypeInfo> types;

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ItemTypeInfo> getTypes() {
        return types;
    }

    public void setTypes(List<ItemTypeInfo> types) {
        this.types = types;
    }
}
