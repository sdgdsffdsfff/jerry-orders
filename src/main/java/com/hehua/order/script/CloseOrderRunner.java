package com.hehua.order.script;

import java.util.Date;
import java.util.List;

import com.hehua.order.service.OrdersService;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hehua.commons.time.DateUtils;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.model.OrdersModel;

/**
 * Created by liuweiwei on 14-8-27. 关闭超过指定时间未付款的订单
 */
public class CloseOrderRunner {

    private Logger log = Logger.getLogger(CloseOrderRunner.class.getName());

    private final int TIMEOUT_SEC = 7200;

    private ClassPathXmlApplicationContext context;

    private OrdersDAO ordersDAO;

    private OrdersService ordersService;

    public CloseOrderRunner() {
        this.init();
    }

    private void init() {
        this.context = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");
        this.ordersDAO = (OrdersDAO) context.getBean("ordersDAO");
        this.ordersService = (OrdersService) context.getBean("ordersService");

    }

    private List<OrdersModel> getToBeClosedOrders() {
        return this.ordersDAO.getToBeClosedOrders(this.TIMEOUT_SEC);
    }

    public void run() {
        log.info("start:" + DateUtils.formatDateTime(new Date()));
        List<OrdersModel> orders = this.getToBeClosedOrders();
        int count = 0;
        for (OrdersModel order : orders) {
            if (this.ordersDAO.updateStatus(order.getId(), OrdersModel.STATUS_CLOSED) != 1) {
                log.error("close order fail, orderid=" + order.getId());
            } else {
                log.info("close order succ, orderid=" + order.getId());
                count++;
            }
            this.ordersService.updateItemQuantity(order.getId(), true);
        }
        log.info("end, processed " + count + " orders");
    }

    public static void main(String[] args) {
        try {
            CloseOrderRunner closeOrderRunner = new CloseOrderRunner();
            closeOrderRunner.run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
