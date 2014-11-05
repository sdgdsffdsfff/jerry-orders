package com.hehua.order.script;

import com.hehua.item.domain.BrandgroupItem;
import com.hehua.item.domain.Flash;
import com.hehua.item.domain.Item;
import com.hehua.item.service.BrandgroupItemService;
import com.hehua.item.service.FlashSessionLocalCache;
import com.hehua.item.service.ItemService;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by liuweiwei on 14-10-17.
 */
public class FlushSalesRunner {

    private ClassPathXmlApplicationContext context;

    private FlashSessionLocalCache flashSessionLocalCache;

    private ItemService itemService;

    private BrandgroupItemService brandgroupItemService;

    private long startTime;

    private static final int MAX_SALES = 500;

    private static final int MIN_SALES = 300;

    private static Logger log = Logger.getLogger(FlushSalesRunner.class);

    public FlushSalesRunner() {
        this.context = new ClassPathXmlApplicationContext("classpath*:/spring/*.xml");
        this.flashSessionLocalCache = context.getBean(FlashSessionLocalCache.class);
        this.itemService = context.getBean(ItemService.class);
        this.brandgroupItemService = context.getBean(BrandgroupItemService.class);
        startTime = System.currentTimeMillis();
    }

    public void run() {
        while (true) {
            List<Flash> flashList = flashSessionLocalCache.get().getFlashes();
            for (Flash flash : flashList) {
                if (!flash.isOnline()) {
                    continue;
                }
                if (flash.isGroup()) {
                    log.info("品牌团开始");
                    List<BrandgroupItem> brandgroupItems = brandgroupItemService.getFlashItemListByBrandGrandId(flash.getGroupid(), -1);
                    for (BrandgroupItem brandgroupItem : brandgroupItems) {
                        Item bitem = itemService.getItemById(brandgroupItem.getItemid());
                        flush(bitem);
                    }
                    log.info("品牌团结束");
                    continue;
                }
                Item item = itemService.getItemById(flash.getItemid());
                flush(item);
            }
            try {
                long sleepTime = genSleepTime();
                log.info("sleep time, " + sleepTime/1000/60 + "m");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - startTime > 1000 * 3600 * 10) {
                break;
            }
        }
    }

    private void flush(Item item) {
        if (item.getSales() > MAX_SALES) {
            return;
        }
        if (!itemService.isSellable(item.getId())) {
            return;
        }
        int oldSales = item.getSales();
        int addSales = item.getSales() == 0 ? genInitSales() : genAddSales(item);
        if (oldSales > MIN_SALES) {
            //达到最小销量，一半的可能继续刷
            if (new Random().nextBoolean()) {
                itemService.incrItemSales((int)item.getId(), addSales);
                log.info(new Date() + " itemid=" + item.getId() + ", origin_sales=" + oldSales + ",add_sales=" + addSales + ",yes");
            } else {
                log.info(new Date() + " itemid=" + item.getId() + ", origin_sales=" + oldSales + ",add_sales=0,no");
            }
        } else {
            //没到达最小销量，无条件刷
            itemService.incrItemSales((int)item.getId(), addSales);
            log.info(new Date() + " itemid=" + item.getId() + ", origin_sales=" + oldSales + ",add_sales=" + addSales + ",normal");
        }
    }

    public int genAddSales(Item item) {
        return new Random().nextInt(16) + 5;
    }

    public long genSleepTime() {
        return (new Random().nextInt(16) + 5) * 60 * 1000;
    }

    public int genInitSales() {
        return new Random().nextInt(20) + 10;
    }

    public static void main(String[] args) {
        FlushSalesRunner runner = new FlushSalesRunner();
        runner.run();
        System.exit(0);
    }
}
