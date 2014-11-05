package com.hehua.order.enums;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-7-25.
 */
public enum OrdersErrorEnum implements MetaCode {

    PARAM_ERROR(40000, "参数错误"),
    PAY_TYPE_ERROR(40001, "支付类型错误"),
    ORDER_NOT_EXIST(40002, "订单不存在"),
    ORDER_HAS_PAYED(40003, "订单已支付"),
    ORDER_STATUS_ERROR(40004, "订单状态错误"),
    ADDRESS_NOT_PRESENT(40005, "没有填写地址"),
    ORDER_NOT_YOURS(40006, "订单不是您的"),
    FEEDBACK_SOCRE_ERROR(40007, "分数错误"),
    FEEDBACK_ORDER_NOT_PAYED(40008, "订单未支付，不能评价"),
    FEEDBACK_CAN_NOT_FEEDBACK(40009, "订单已经评价过了"),
    FEEDBACK_NOT_EXIST(40010, "评价不存在"),
    FEEDBACK_SAME_WITH_LAST(40011, "评价内容与上次一致"),
    DELIVERY_COMPANY_NOT_EXISTS(40012, "物流公司不存在"),
    ADDRESS_NOT_EXIST(40013, "收货地址不存在"),
    ADDRESS_NOT_YOURS(40014, "收货地址不是您的"),
    SKU_NOT_EXIST(40015, "SKU不存在"),
    QUANTITY_ERROR(40016, "数量错误"),
    ITEM_CAN_NOT_BUY(40017, "商品不可购买"),
    ITEM_NOT_EXIST(40018, "商品不存在"),
    ORDER_PAY_ERROR(40019, "支付错误"),
    ORDER_FEEDBACK_ERROR(40020, "还未购买不能评价"),
    ORDER_DELIVERYINFO_NOT_EXITS(40021, "订单物流地址不存在"),
    ITEM_NOT_FROM_CART(40022, "请先将商品加入购物车"),
    SKU_QUANTITY_ERROR(40023, "商品库存不足"),
    ORDER_CLOSED_ERROR(40024, "超时未付款，订单已关闭"),
    REFUND_NOT_EXISTS(40025, "退款不存在"),
    REFUND_STATUS_ERROR(40026, "退款状态错误"),
    NOTIFYSUCCESSLOG_NOT_FOUND(40027, "支付路径未找到"),
    REFUND_OUTNO_EXPIRED(40028, "退款批次号已过期"),
    REFUND_SENDTO_PAIU_FAIL(40029, "退款信息发送至派友失败"),
    INVOICE_NOT_PRESENT(40030, "发票抬头不能为空"),
    CREDIT_NOT_EXISTS(40031, "用户余额不存在"),
    CREDIT_LESS_ERROR(40032, "用户余额不足"),
    QUANTITY_RESTRICTION(40033, "该商品只能购买1件哦")
    ;

    private final int code;
    private final String msg;

    private OrdersErrorEnum(int code, String msg) {
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
