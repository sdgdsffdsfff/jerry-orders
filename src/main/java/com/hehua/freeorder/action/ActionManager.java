package com.hehua.freeorder.action;

import com.hehua.freeorder.exceptions.NoSuchFreeOrderActionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuweiwei on 14-10-6.
 */
@Named
public class ActionManager implements BeanPostProcessor {

    private Map<Integer, ActionBase> actionMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ActionBase) {
            ActionBase actionBase = (ActionBase)bean;
            this.actionMap.put(actionBase.getAction(), actionBase);
        }
        return bean;
    }

    public ActionBase getAction(int action) throws NoSuchFreeOrderActionException {
        if (actionMap.get(action) == null) {
            throw new NoSuchFreeOrderActionException("freeorder action " + action + " not found!");
        }
        return actionMap.get(action);
    }
}
