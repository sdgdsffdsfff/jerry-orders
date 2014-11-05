package com.hehua.order.script;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * Created by liuweiwei on 14-9-2.
 * 查询快递100脚本
 */
public class QueryKuaidi100 {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");

        DefaultMessageListenerContainer messageListenerContainer = (DefaultMessageListenerContainer)applicationContext.getBean("queryKuaidi100MessageListener");

        messageListenerContainer.start();

    }

}
