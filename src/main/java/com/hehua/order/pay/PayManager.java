package com.hehua.order.pay;

import com.hehua.order.exceptions.NoSuchPayConfigException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuweiwei on 14-8-14.
 */
@Named
public class PayManager implements BeanPostProcessor {

    private Map<Integer, BasePayProvider> payMapByType = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof BasePayProvider) {
            BasePayProvider pay = (BasePayProvider) bean;
            payMapByType.put(pay.getPayType(), pay);
        }
        return bean;
    }

    public BasePayProvider getPayObj(int paytype) throws NoSuchPayConfigException {
        if (payMapByType.get(paytype) == null) {
            throw new NoSuchPayConfigException("paytype not found:" + paytype);
        }
        return payMapByType.get(paytype);
    }
}

