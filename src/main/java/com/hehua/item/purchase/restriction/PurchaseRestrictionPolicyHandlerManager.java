/**
 * 
 */
package com.hehua.item.purchase.restriction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zhihua
 *
 */
@Component
public class PurchaseRestrictionPolicyHandlerManager implements BeanPostProcessor {

    private Map<PurchaseRestrictionPolicyType, PurchaseRestrictionPolicyHandler> handlersMap = new HashMap<>();

    public PurchaseRestrictionPolicyHandler getPurchaseRestrictionPolicyHandler(
            PurchaseRestrictionPolicyType type) {
        return handlersMap.get(type);
    }

    public PurchaseRestrictionPolicyHandler getPurchaseRestrictionPolicyHandler(String type) {
        return handlersMap.get(PurchaseRestrictionPolicyType.valueOf(type));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof PurchaseRestrictionPolicyHandler) {
            PurchaseRestrictionPolicyHandler purchaseRestrictionPolicyHandler = (PurchaseRestrictionPolicyHandler) bean;
            handlersMap.put(purchaseRestrictionPolicyHandler.getType(),
                    purchaseRestrictionPolicyHandler);
        }
        return bean;
    }

}
