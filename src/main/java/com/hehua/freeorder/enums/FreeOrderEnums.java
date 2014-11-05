package com.hehua.freeorder.enums;

import com.hehua.commons.model.MetaCode;

/**
 * Created by liuweiwei on 14-10-6.
 */
public enum  FreeOrderEnums implements MetaCode {

    PARAM_ERROR(50000, "参数错误"),
    ORDER_NOT_FINISHED(50001, "之前的评测完成了才可以继续申请哦"),
    ADDRESS_NOT_EXIST(50002, "地址不存在"),
    ADDRESS_NOT_YOURS(50003, "地址不是你的"),
    FREE_FLASH_SOLDOUT(50004, "此产品当前不能申请"),
    FREE_ORDER_STATUS_ERROR(50005, "订单状态错误"),
    ;

    private final int code;
    private final String msg;

    private FreeOrderEnums(int code, String msg) {
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
