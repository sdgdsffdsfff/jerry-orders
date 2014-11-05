package com.hehua.order.service;

import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.CreditLogDAO;
import com.hehua.order.info.CreditLogInfo;
import com.hehua.order.model.CreditLogModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuweiwei on 14-8-16.
 */
@Named
public class CreditLogService {

    @Inject
    CreditLogDAO creditLogDAO;

    public List<CreditLogInfo> getListByUserId(long userid, int offset, int limit) {
        List<CreditLogInfo> creditLogInfos = new ArrayList<>();
        List<CreditLogModel> creditLogModels = creditLogDAO.getListByUserId(userid, offset, limit);
        for (CreditLogModel creditLogModel : creditLogModels) {
            CreditLogInfo creditLogInfo = new CreditLogInfo();
            creditLogInfo.setTime(DateUtils.formatTimestamp((long)creditLogModel.getAddtime(), false));
            creditLogInfo.setBalance(creditLogModel.getBalance());
            creditLogInfo.setMoney(creditLogModel.getValue());
            creditLogInfo.setSummary(CreditLogModel.descType.get(creditLogModel.getType()));
            creditLogInfos.add(creditLogInfo);
        }
        return creditLogInfos;
    }
}
