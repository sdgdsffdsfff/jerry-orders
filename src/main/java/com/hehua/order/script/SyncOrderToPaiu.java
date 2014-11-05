package com.hehua.order.script;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * Created by liuweiwei on 14-8-25.
 */
public class SyncOrderToPaiu {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");

        DefaultMessageListenerContainer messageListenerContainer = (DefaultMessageListenerContainer)applicationContext.getBean("syncOrderToPaiuMessageListener");

        messageListenerContainer.start();

    }
}
