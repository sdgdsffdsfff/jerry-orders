package com.hehua.order.constants;

import com.hehua.framework.config.ZookeeperConfigManager;

/**
 * Created by liuweiwei on 14-7-31.
 * 一些主机常量配置
 */
public class Host {

    public final static String API_HOST = "api.hehuababy.com";

    public final static String API_HOST_KEY = "api_host";

    public static String getApiHost() {
        return ZookeeperConfigManager.getInstance().getString(API_HOST_KEY);
    }
}
