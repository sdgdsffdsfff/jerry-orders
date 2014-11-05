package com.hehua.order.constants;

import com.hehua.framework.config.ZookeeperConfigManager;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuweiwei on 14-8-25.
 */
public class PaiyouConfig {

    private static final String prefix = "http://top.paiu.com.cn/oms/api/hehua/";
    //public static final String ORDER_SYNC_URL = prefix + "synOrder.py";
    public static final String ORDER_SYNC_URL = "http://top.paiu.com.cn/wms/api/hehua/synOrder.py";

    //public static final String RETURN_GOODS_URL = prefix + "returnGoods.py";
    public static final String RETURN_GOODS_URL = "http://top.paiu.com.cn/wms/api/hehua/returnGoods.py";


    public static final String APPKEY = "hehua";

    public static final String APPSECKET = "0cda78bcca322f7ab9c202745a042c2e";

    public static final String hehua_paiu_appkey = "paiu";
    public static final String hehua_paiu_appsecret = "9c5167642ba1cdca16aab18de8138609"; //^yya25@12bs

    public static final String SYNC_ORDER_KEY = "paiu_sync_order_url";
    public static final String RETURN_GOODS_KEY = "paiu_return_goods_url";
    public static final String SWITCH_KEY = "paiu_switch";

    public static String getSyncOrderUrl() {
        return ZookeeperConfigManager.getInstance().getString(SYNC_ORDER_KEY);
    }

    public static String getReturnGoodsUrl() {
        return ZookeeperConfigManager.getInstance().getString(RETURN_GOODS_KEY);
    }

    public static boolean isSwitchOn() {
        String paiuSwitch = ZookeeperConfigManager.getInstance().getString(SWITCH_KEY);
        if (StringUtils.isBlank(paiuSwitch)) {
            return false;
        }
        if (paiuSwitch.equalsIgnoreCase("on")) {
            return true;
        }
        return false;
    }
}
