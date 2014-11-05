package com.hehua.freeorder.action;

import com.hehua.freeorder.model.FreeOrderModel;

/**
 * Created by liuweiwei on 14-10-6.
 * 众测订单相关动作
 */

public interface Action<T1, T2> {

    public boolean beforeExecute(FreeOrderModel order, T1 params, T2 result);

    public boolean execute(FreeOrderModel order, T1 params, T2 result);

    public boolean afterExecute(FreeOrderModel order, T1 params);
}

