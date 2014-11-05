package com.hehua.order.script;

import com.hehua.commons.time.DateUtils;
import com.hehua.commons.utils.EmailSender;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.service.OrdersService;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import java.util.*;

/**
 * Created by liuweiwei on 14-9-18.
 */
public class OrderMonitorRunner {

    private Logger log = Logger.getLogger(OrderMonitorRunner.class.getName());

    private int INTERVAL = 1800;

    private ClassPathXmlApplicationContext context;

    private OrdersDAO ordersDAO;

    private OrdersService ordersService;

    private String to = "yixiao@hehuababy.com";

    private String subject = "过去半小时订单状态统计";

    private int begin;

    private int end;

    public OrderMonitorRunner(String[] receiver) {
        if (receiver.length > 0) {
            this.to = receiver[0];
        }
        if (receiver.length > 1) {
            this.INTERVAL = Integer.parseInt(receiver[1]);
            this.subject = "过去" + (this.INTERVAL/3600) + "小时订单状态统计";
        }
        this.init();
    }

    private void init() {
        this.context = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");
        this.ordersDAO = (OrdersDAO) context.getBean("ordersDAO");
        this.ordersService = (OrdersService) context.getBean("ordersService");
        this.end = DateUtils.unix_timestamp();
        this.begin = end - INTERVAL;
    }

    private List<OrdersModel> getOrderList() {
        return this.ordersDAO.getOrdersByOrdertime(this.begin, this.end);
    }

    public void run() {
        log.info("start:" + DateUtils.formatDateTime(new Date()));
        List<OrdersModel> orders = this.getOrderList();
        HashMap<Integer, Integer> statusCountMap = new HashMap<>();
        for (OrdersModel order : orders) {
            int count = 0;
            if (statusCountMap.get(order.getStatus()) != null) {
                count = statusCountMap.get(order.getStatus());
                count += 1;
            } else {
                count = 1;
            }
            statusCountMap.put(order.getStatus(), count);
        }
        String html = this.renderTable(statusCountMap);
        try {
            EmailSender.getInstance().sendHtml(this.to, this.subject, html);
        } catch (MessagingException e) {
            log.error("send mail fail", e);
        }
        log.info("end");
        log.info(html);
    }

    public String renderTable(HashMap<Integer, Integer> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body>");
        sb.append("<p><h1>");
        sb.append(this.subject);
        sb.append("</h1></p><hr />");
        sb.append("<table border=\"1\">");
        this.renderTr(sb, "状态", "数量");
        Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator();
        int total = 0;
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> entry = iter.next();
            this.renderTr(sb, OrdersModel.statusDesc.get(entry.getKey()), String.valueOf(entry.getValue()));
            total += entry.getValue();
        }
        this.renderTr(sb, "订单总数", String.valueOf(total));
        sb.append("</table>");
        sb.append("</body></html>");
        return sb.toString();
    }

    public void renderTr(StringBuffer sb, String key, String value) {
        sb.append("<tr><th>");
        sb.append(key);
        sb.append("</th>");
        sb.append("<td>");
        sb.append(value);
        sb.append("</td></tr>");
    }

    public static void main(String[] args) {
        try {
            OrderMonitorRunner orderMonitorRunner = new OrderMonitorRunner(args);
            orderMonitorRunner.run();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
