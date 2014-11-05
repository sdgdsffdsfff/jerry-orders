package com.hehua.freeorder.action.params;

import com.hehua.freeorder.info.ApplyTipInfo;
import com.hehua.order.info.AddressInfo;

import java.util.List;

/**
 * Created by liuweiwei on 14-10-7.
 * 申请众测确认返回参数
 */
public class ApplyGetParam {

    private AddressInfo address;
    private List<ApplyTipInfo> tips;

    public List<ApplyTipInfo> getTips() {
        return tips;
    }

    public void setTips(List<ApplyTipInfo> tips) {
        this.tips = tips;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }
}
