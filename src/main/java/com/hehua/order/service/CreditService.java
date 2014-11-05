package com.hehua.order.service;

import com.hehua.order.dao.CreditDAO;
import com.hehua.order.dao.CreditLogDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.exceptions.CreditException;
import com.hehua.order.exceptions.CreditNotExistException;
import com.hehua.order.listener.PayNotifyListener;
import com.hehua.order.model.CreditLogModel;
import com.hehua.order.model.CreditModel;
import com.hehua.order.model.NotifyModel;
import com.hehua.order.model.OrdersModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import org.apache.log4j.Logger;

/**
 * Created by liuweiwei on 14-8-15.
 * 用户账户余额服务
 */
@Named
public class CreditService {

    private Logger log = Logger.getLogger(CreditService.class.getName());

    @Inject
    private CreditDAO creditDAO;

    @Inject
    private CreditLogDAO creditLogDAO;

    @Inject
    private OrdersDAO ordersDAO;

    /**
     * 检查用户余额账户是否存在，不存在则创建一个
     * @param userid 用户ID
     * @return 存在返回true，否则false
     */
    public boolean updateCheckAndCreate(long userid) {
        CreditModel creditModel = creditDAO.getByUserid(userid);
        if (creditModel == null) {
            creditModel = new CreditModel();
            creditModel.initWhenAdd(userid);
            creditDAO.add(creditModel);
            return false;
        }
        return true;
    }

    /**
     * 更新账户余额
     * @param userid
     * @param value
     * @param rebate
     * @param giftcard
     * @param recharge
     * @param direct
     * @return
     * @throws CreditException
     */
    public BigDecimal updateUpdate(long userid, BigDecimal value, BigDecimal rebate, BigDecimal giftcard, BigDecimal recharge, BigDecimal direct) throws CreditException {
        CreditModel credit = creditDAO.getByUserid(userid);
        if (credit == null) {
            throw new CreditNotExistException(userid + "");
        }
        credit.setValue(value.add(credit.getValue()));
        credit.setRebate(rebate.add(credit.getRebate()));
        credit.setGiftcard(giftcard.add(credit.getGiftcard()));
        credit.setRecharge(recharge.add(credit.getRecharge()));
        credit.setDirect((direct.add(credit.getDirect())));
        credit.setModtime((int)(System.currentTimeMillis()/1000L));
        int affected_rows = creditDAO.update(credit);
        if (affected_rows != 1) {
            throw new CreditException("sql error");
        }
        return credit.getValue();
    }

    /**
     * 注意：
     * 订单支付处理，仅作余额变更操作，不处理业务逻辑判断
     * @see com.hehua.order.listener.PayNotifyListener#processPay(long)
     * @param notify 通知记录
     * @return
     */
    public boolean pay(NotifyModel notify) {
        Logger log = Logger.getLogger(PayNotifyListener.class.getName());
        OrdersModel order = ordersDAO.getById(notify.getObjid());
        if (order == null) {
            log.error("order not exist:" + notify.toString());
            return false;
        }
        CreditModel credit = creditDAO.getByUserid(order.getUserid());
        if (credit == null) {
            log.error("credit not exist:" + notify.toString());
            return false;
        }
        BigDecimal zero = new BigDecimal(0);
        BigDecimal balance;
        try {
            balance = this.updateUpdate(order.getUserid(), notify.getMoney(), zero, zero, zero, notify.getMoney());
        } catch (CreditException e) {
            log.error("sql error:" + notify.toString());
            return false;
        }
        CreditLogModel creditlog = new CreditLogModel();
        creditlog.setUserid(order.getUserid());
        creditlog.setType(CreditLogModel.TYPE_PAY);
        creditlog.setObjid(order.getId());
        creditlog.setValue(notify.getMoney());
        creditlog.setBalance(balance);
        creditlog.setRebate(zero);
        creditlog.setGiftcard(zero);
        creditlog.setRecharge(zero);
        creditlog.setDirect(notify.getMoney());
        creditlog.setAddtime((int)(System.currentTimeMillis()/1000L));
        if (creditLogDAO.add(creditlog) != 1) {
            log.error("sql error:" + creditlog.toString());
            return false;
        }
        return true;
    }
}
