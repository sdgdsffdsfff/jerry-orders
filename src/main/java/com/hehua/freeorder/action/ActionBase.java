package com.hehua.freeorder.action;

import com.hehua.commons.time.DateUtils;
import com.hehua.freeorder.dao.FreeOrderDAO;
import com.hehua.freeorder.dao.FreeOrderLogDAO;
import com.hehua.freeorder.model.FreeOrderLogModel;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.freeorder.service.FreeOrderService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;

/**
 * Created by liuweiwei on 14-10-6.
 * 众测订单动作基类
 */
@Named
public abstract class ActionBase<T1, T2> implements Action<T1, T2> {

    @Inject
    protected FreeOrderDAO freeOrderDAO;

    @Inject
    protected FreeOrderLogDAO freeOrderLogDAO;

    @Inject
    protected FreeOrderService freeOrderService;

    /* 申请动作 */
    public static final int ACTION_APPLY = 1;
    /* 审核通过 */
    public static final int ACTION_APPROVE1 = 2;
    /* 审核拒绝 */
    public static final int ACTION_REJECT1 = 4;
    /* 已发货 */
    public static final int ACTION_DELIVERY = 6;
    /* 已签收 */
    public static final int ACTION_SIGN = 8;
    /* 评测编辑中 */
    public static final int ACTION_EDIT = 3;
    /* 提交评测 */
    public static final int ACTION_COMMIT = 5;
    /* 评测审核通过 */
    public static final int ACTION_APPROVE2 = 10;
    /* 评测审核拒绝*/
    public static final int ACTION_REJECT2 = 12;

    public static HashMap<Integer, String> descAction = new HashMap<Integer, String>() {
        {
            put(1, "新申请");
            put(2, "审核通过");
            put(3, "审核拒绝");
            put(4, "已发货");
            put(5, "已签收");
            put(6, "保存评测");
            put(8, "提交评测");
            put(10, "评测审核通过");
            put(12, "评测审核拒绝");
        }
    };

    public abstract int getAction();

    public boolean doAction(FreeOrderModel order, T1 params, T2 result) {

        if (!this.beforeExecute(order, params, result)) {
            return false;
        }

        if (!this.execute(order, params, result)) {
            return false;
        }

        if (!this.afterExecute(order, params)) {
            return false;
        }

        return true;
    }

    public boolean afterExecute(FreeOrderModel order, T1 params) {
        FreeOrderLogModel orderlog = new FreeOrderLogModel();
        orderlog.setFreeOrderId(order.getId());
        orderlog.setType(getAction());
        orderlog.setOpid(0);
        orderlog.setComment("");
        orderlog.setAddtime(DateUtils.unix_timestamp());
        freeOrderLogDAO.add(orderlog);
        return true;
    }

}
