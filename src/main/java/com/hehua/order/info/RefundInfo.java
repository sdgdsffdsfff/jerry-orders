package com.hehua.order.info;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-7.
 */
public class RefundInfo {
    private long id;
    private int status;
    private String statusName;
    private BigDecimal money;
    private List<RefundEventInfo> event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public List<RefundEventInfo> getEvent() {
        return event;
    }

    public void setEvent(List<RefundEventInfo> event) {
        this.event = event;
    }
}
