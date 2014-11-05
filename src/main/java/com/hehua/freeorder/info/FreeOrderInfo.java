package com.hehua.freeorder.info;

import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.order.info.AddressInfo;
import com.hehua.order.info.DeliveryInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-10-7.
 */
public class FreeOrderInfo {

    private AddressInfo address;
    private List<FreeFlashInfo> freeFlashInfo;
    private DeliveryInfo deliveryInfo;
    private int applyTime;
    private int freeOrderid;
    private int status;

    public static String convertStatus(int status) {
        return FreeOrderModel.descStatus.get(status);
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }

    public List<FreeFlashInfo> getFreeFlashInfo() {
        return freeFlashInfo;
    }

    public void setFreeFlashInfo(List<FreeFlashInfo> freeFlashInfo) {
        this.freeFlashInfo = freeFlashInfo;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public int getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(int applyTime) {
        this.applyTime = applyTime;
    }

    public int getFreeOrderid() {
        return freeOrderid;
    }

    public void setFreeOrderid(int freeOrderid) {
        this.freeOrderid = freeOrderid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
