package com.hehua.order.service;

import com.hehua.framework.jms.JmsApi;
import com.hehua.order.dao.NotifyDAO;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.pay.NotifyId;
import com.hehua.order.pay.PayManager;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

/**
 * Created by liuweiwei on 14-7-31.
 */
@Named
public class NotifyService {

    static Logger log = Logger.getLogger("notifyLogFile");

    @Inject
    NotifyDAO notifyDAO;

    @Inject
    PayManager payManager;

    @Inject
    JmsApi jmsApi;

    public boolean saveNotify(String paytype, String url) {
        log.info("notify data:" + url);
        BasePayProvider payObj = null;
        try {
            payObj = payManager.getPayObj(BasePayProvider.payNameIdMap.get(paytype));
        } catch (Exception e) {
            log.error("paytype error", e);
            return false;
        }
        NotifyId notifyid = new NotifyId();
        boolean ret = payObj.saveNotify(url, notifyid);
        if (!ret) {
            return ret;
        }
        jmsApi.call("PayNotify", notifyid.getId());
        if (log.isDebugEnabled()) {
            System.out.println("notifyid=" + notifyid.getId());
        }
        return true;
    }
}
