package com.hehua.order.refund;

import com.hehua.order.model.RefundModel;
import com.hehua.order.service.RefundService;
import org.apache.log4j.Logger;

/**
 * Created by liuweiwei on 14-9-22.
 * 退款基类
 */
public abstract class RefundBase<T1, T2> implements Action<T1, T2> {

    /* 新增退款 */
    public static final int ACTION_APPLY = 0;
    /* 一审通过 */
    public static final int ACTION_APPROVE1 = 1;
    /* 一审拒绝 */
    public static final int ACTION_REJECT1 = 3;
    /* 已付款，待结果 */
    public static final int ACTION_WAITING = 5;
    /* 退款成功 */
    public static final int ACTION_SUCCESS = 7;
    /* 退款失败 */
    public static final int ACTION_FAIL = 9;
    /* 二审通过 */
    public static final int ACTION_APPROVE2 = 11;
    /* 二审拒绝 */
    public static final int ACTION_REJECT2 = 13;

    /* 取消退款 */
    public static final int ACTION_CANCEL = 2;
    /* 用户回寄商品 */
    public static final int ACTION_RETURNGOODS = 4;

    protected static Logger log = Logger.getLogger(RefundService.class.getName());

    public boolean doAction(RefundModel refund, T1 params, T2 result) {

        if (!this.beforeExecute(refund, params, result)) {
            return false;
        }

        if (!this.execute(refund, params, result)) {
            return false;
        }

        if (!this.afterExecute(refund, params)) {
            return false;
        }

        return true;
    }

    public abstract int getAction();
}
