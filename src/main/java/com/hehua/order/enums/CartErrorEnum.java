package com.hehua.order.enums;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-7-27.
 */
public enum CartErrorEnum implements MetaCode {


    ITEM_NOT_EXIST(42000, "产品不存在"),
    ITEMID_NOT_PRESENT(42001, "需要输入产品ID"),
    ITEM_NOT_YOURS(42002, "不是您的商品"),
    SKUID_NOT_PRESENT(42003, "需输入skuid"),
    SKU_QUANTITY_NOT_PRESENT(42004, "需输入商品数量"),
    PARAM_TYPE_ERROR(42005, "参数类型错误"),
    SKU_NOT_EXIST(42006, "SKU不存在"),
    QUANTITY_ERROR(42007, "数量错误"),
    ITEM_CAN_NOT_BUY(42008, "商品不可购买"),
    QUANTITY_RESTRICTION(42009, "该商品只能购买1件哦")
    ;

    private final int code;
    private final String msg;

    private CartErrorEnum(int code, String msg) {
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
