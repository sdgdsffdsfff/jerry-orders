package com.hehua.order.info;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-8-16.
 */
public class CreditLogInfo {

    private String time;
    private String summary;
    private BigDecimal money;
    private BigDecimal balance;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
