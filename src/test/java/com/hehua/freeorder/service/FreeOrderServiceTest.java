package com.hehua.freeorder.service;

import com.google.common.collect.ListMultimap;
import com.hehua.freeorder.value.FreeOrderValue;
import com.hehua.order.dao.DeliveryInfoDAO;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.service.OrderService;
import com.peaceful.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FreeOrderServiceTest {

    ConfigurableApplicationContext context;
    FreeOrderService freeOrderService;
    DeliveryInfoDAO deliveryInfoDAO;

    OrderService orderService;


    @Before
    public void before() {
        context = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext-*.xml");
        freeOrderService = (FreeOrderService) context.getBean("freeOrderService");
        deliveryInfoDAO = context.getBean(DeliveryInfoDAO.class);
        orderService = context.getBean(OrderService.class);

    }

    @Test
    public void testGetFreeOrderByFreeOrderId() throws Exception {
        FreeOrderValue freeOrderValue = freeOrderService.getFreeOrderByFreeOrderId(11);
        Util.report(freeOrderValue.getItemAppraise().getAppraise());
    }

    @Test
    public void testGetFreeOrderByFreeId() throws Exception {
        System.out.println(freeOrderService.getNewApplyUser().size());
        List<Long> userIds = new ArrayList<>(2);
        userIds.add(123579l);
        //freeOrderService.get
    }

    @Test
    public void testGetNotApplyItemByItemIdsAndUserId() {
        List<Integer> itemids = new ArrayList<Integer>();
        itemids.add(117);
        itemids.add(127);
        itemids.add(137);
        itemids.add(147);

        //System.out.println(freeOrderService.getNotApplyItemByItemIdsAndUserId(123579l,itemids));
    }

    @Test
    public void testBatchDeliveryInfo() {
        Collection<Long> orderIds = new ArrayList();
        orderIds.add(132l);
        orderIds.add(133l);
        System.out.println(deliveryInfoDAO.getListByOrderIds(orderIds));
    }

    @Test
    public void testOrderItemByOrderIds() {
        List<Long> orderIds = new ArrayList<Long>();
        orderIds.add(161l);
        orderIds.add(143l);
        orderIds.add(144l);
        ListMultimap<Long, OrderItemsModel> multiList = orderService.getOrderItemByOrderId(orderIds);
        for (Long orderId : multiList.keySet()) {

            System.out.println("key=" + orderId + "  " + multiList.get(orderId));
        }
    }
}