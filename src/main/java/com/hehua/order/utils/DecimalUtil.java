package com.hehua.order.utils;

import java.math.BigDecimal;

/**
 * Created by liuweiwei on 14-9-2.
 */
public class DecimalUtil {

    /**
     * 返回小数点后两位，四舍五入
     */
    public static BigDecimal toStandard(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
