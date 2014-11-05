package com.hehua.order.enums;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-8-9.
 */
public enum RefundErrorEnum implements MetaCode {


    PARAM_ERROR(44000, "参数错误"),
    ORDER_NOT_EXIST(44001, "订单不存在"),
    CAN_NOT_APPLY(44002, "订单不能申请退款"),
    API_EXPIRED(44003, "请升级手机版本以使用退款功能"),
    SKU_NOT_MATCH_ORDER(44004, "商品不属于此订单"),
    REFUND_NUM_ERROR(44005, "退款数量错误"),
    REFUND_REASON_ERROR(44006, "退款原因错误"),
    REFUND_NOT_YOURS(44007, "退款不是您的"),
    REFUND_CANNOT_CANCEL(44008, "不能取消退款"),
    REFUND_APPROVE2_ERROR(44009, "审核通过之后才能操作收货"),
    REFUND_NOT_EXISTS(44010, "退款不存在"),
    REFUND_APPROVE1_ERROR(44011, "只有新申请的退款才能批准或拒绝"),
    REFUND_WITHDRAW_STATUS_ERROR(44012, "只有收货成功的退款才能点击"),
    REFUND_STATUS_ERROR(44013, "退款状态错误"),
    REFUND_DELIVERY_COMPANY_ERROR(44014, "物流公司不存在"),
    REFUND_DELIVERY_DELIVERYNUM_NOT_EXISTS(44014, "物流单号不存在"),
    REFUND_DELIVERY_COMPANY_NOT_SUPPORT(44015, "物流公司不支持"),
    REFUND_MONEY_ERROR(44016, "退款金额错误"),
    ;

    private final int code;
    private final String msg;

    private RefundErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
