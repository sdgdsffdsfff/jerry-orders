package com.hehua.order.refund;

import com.hehua.order.model.RefundModel;

/**
 * Created by liuweiwei on 14-9-22.
 * 退款相关动作接口
 */
public interface Action<T1, T2> {

    public boolean beforeExecute(RefundModel refund, T1 params, T2 result);

    public boolean execute(RefundModel refund, T1 params, T2 result);

    public boolean afterExecute(RefundModel refund, T1 params);
}
