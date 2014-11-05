package com.hehua.order.info;

import com.google.common.base.Function;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-9-23.
 */
public class RefundItemInfo {

    private long skuid;
    private long itemid;
    private String title;
    private BigDecimal price;
    private String image;
    private int canRefundAmount;
    private List<ItemTypeInfo> types;

    public static Function<RefundItemInfo, Long> skuIdExtrator = new Function<RefundItemInfo, Long>() {
        @Override
        public Long apply(RefundItemInfo input) {
            return input.getSkuid();
        }
    };

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

    public void setImage(String image) {
        this.image = image;
    }

    public int getCanRefundAmount() {
        return canRefundAmount;
    }

    public void setCanRefundAmount(int canRefundAmount) {
        this.canRefundAmount = canRefundAmount;
    }

    public List<ItemTypeInfo> getTypes() {
        return types;
    }

    public void setTypes(List<ItemTypeInfo> types) {
        this.types = types;
    }
}
