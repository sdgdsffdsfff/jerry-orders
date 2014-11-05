package com.hehua.order.service.params;

/**
 * Created by liuweiwei on 14-8-23.
 */
public class DeliveryServiceFillDeliveryInfoParam {

    private String appkey;
    private String appsecret;
    private String data;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeliveryServiceFillDeliveryInfoParam{" +
                "appkey='" + appkey + '\'' +
                ", appsecret='" + appsecret + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
