package com.hehua.freeorder.model;

import com.google.common.base.Function;
import com.hehua.commons.time.DateUtils;

import java.util.HashMap;

/**
 * Created by liuweiwei on 14-10-6.
 */
public class FreeOrderModel {

    private int id;
    private int freeFlashId;
    private long userid;
    private long itemid;
    private long skuid;
    private int status;
    private long applyip;
    private String deliveryInfo;
    private String addressInfo;
    private int addtime;

    public static Function<FreeOrderModel, Long> userIdExtractor = new Function<FreeOrderModel, Long>() {

        @Override
        public Long apply(FreeOrderModel input) {
            return input.getUserid();
        }
    };


    /* 新申请 */
    public static final int STATUS_NEW = 0;
    /* 审核通过 */
    public static final int STATUS_APPRVOE1 = 2;
    /* 审核拒绝 */
    public static final int STATUS_REJECT1 = 4;
    /* 已发货 */
    public static final int STATUS_DELIVERY = 6;
    /* 已签收 */
    public static final int STATUS_SIGN = 8;
    /* 评测编辑中 */
    public static final int STATUS_EDIT = 10;
    /* 评测编辑完成 */
    public static final int STATUS_COMMIT = 12;
    /* 评测审核通过 */
    public static final int STATUS_APPROVE2 = 14;
    /* 评测审核拒绝 */
    public static final int STATUS_REJECT2 = 16;

    public static HashMap<Integer, String> descStatus = new HashMap<Integer, String>() {
        {
            put(0, "新申请");
            put(2, "审核通过");
            put(4, "审核拒绝");
            put(6, "已发货");
            put(8, "已签收");
            put(10, "评测编辑中");
            put(12, "已完成");
            put(14, "评测审核通过");
            put(16, "评测审核拒绝");
        }
    };

    public boolean hasDeliveried() {
        return this.status >= STATUS_DELIVERY;
    }

    public void setDeliveried() {
        this.status = STATUS_DELIVERY;
    }

    public void setSigned() {
        this.status = STATUS_SIGN;
    }

    public void setApproved1() {
        this.status = STATUS_APPRVOE1;
    }

    public void setReject1() {
        this.status = STATUS_REJECT1;
    }

    public void setEdit() {
        this.status = STATUS_EDIT;
    }

    public void setCommit() {
        this.status = STATUS_COMMIT;
    }

    public void setApproved2() {
        this.status = STATUS_APPROVE2;
    }

    public void setReject2() {
        this.status = STATUS_REJECT2;
    }

    public boolean isStatusNew() {
        return this.status == STATUS_NEW;
    }

    public boolean isStatusApprove1() {
        return this.status == STATUS_APPRVOE1;
    }

    public boolean isStatusDeliveried() {
        return this.status == STATUS_DELIVERY;
    }

    public boolean isStatusSigned() {
        return this.status == STATUS_SIGN;
    }

    public boolean isStatusComplete() {
        return this.status == STATUS_COMMIT;
    }
    public void initWhenAdd() {
        this.status = STATUS_NEW;
        this.deliveryInfo = "{}";
        this.addtime = DateUtils.unix_timestamp();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeFlashId() {
        return freeFlashId;
    }

    public void setFreeFlashId(int freeFlashId) {
        this.freeFlashId = freeFlashId;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public long getSkuid() {
        return skuid;
    }

    public void setSkuid(long skuid) {
        this.skuid = skuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getApplyip() {
        return applyip;
    }

    public void setApplyip(long applyip) {
        this.applyip = applyip;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
