package com.hehua.order.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.inject.Named;

/**
 * Created by liuweiwei on 14-9-2.
 * 商品事务管理
 */
@Named
public class ItemTransactionManagerUtil {

    @Autowired
    @Qualifier("itemTransactionManager")
    private DataSourceTransactionManager txManager;

    public TransactionStatus getTxStatus() {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return txManager.getTransaction(txDef);
    }

    public void commit(TransactionStatus txStatus) {
        txManager.commit(txStatus);
    }

    public void rollback(TransactionStatus txStatus) {
        txManager.rollback(txStatus);
    }
}
