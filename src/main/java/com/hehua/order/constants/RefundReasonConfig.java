package com.hehua.order.constants;

import com.hehua.order.info.RefundReasonInfo;

import java.util.*;

/**
 * Created by liuweiwei on 14-9-23.
 */
public class RefundReasonConfig {

    public static HashMap<Integer, String> reaseonMap = new HashMap<Integer, String>() {
        {
            put(1, "收到商品与描述不符");
            put(2, "商品错发");
            put(3, "需要维修");
            put(4, "发票问题");
            put(5, "质量问题");
        }
    };

    public static List<RefundReasonInfo> genReasonInfo() {
        Iterator<Map.Entry<Integer, String>> iter = reaseonMap.entrySet().iterator();
        List<RefundReasonInfo> refundReasonInfos = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry<Integer, String> entry = iter.next();
            RefundReasonInfo r = new RefundReasonInfo();
            r.setId(entry.getKey());
            r.setDesc(entry.getValue());
            refundReasonInfos.add(r);
        }
        return refundReasonInfos;
    }
}
