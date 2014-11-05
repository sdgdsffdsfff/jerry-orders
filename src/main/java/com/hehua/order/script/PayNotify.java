package com.hehua.order.script;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * Created by liuweiwei on 14-8-4.
 * 处理支付通知脚本
 */
public class PayNotify {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");

        DefaultMessageListenerContainer messageListenerContainer = (DefaultMessageListenerContainer)applicationContext.getBean("payNotifyMessageListener");

        messageListenerContainer.start();

    }
}