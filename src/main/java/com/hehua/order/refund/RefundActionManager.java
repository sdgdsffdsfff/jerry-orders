package com.hehua.order.refund;

import com.hehua.order.exceptions.NoSuchRefundActionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuweiwei on 14-9-22.
 */
@Named
public class RefundActionManager implements BeanPostProcessor {

    private Map<Integer, RefundBase> refundActionMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RefundBase) {
            RefundBase refundBase = (RefundBase)bean;
            this.refundActionMap.put(refundBase.getAction(), refundBase);
        }
        return bean;
    }

    public RefundBase getAction(int action) throws NoSuchRefundActionException {
        if (refundActionMap.get(action) == null) {
            throw new NoSuchRefundActionException("refund action " + action + " not found!");
        }
        return refundActionMap.get(action);
    }
}
