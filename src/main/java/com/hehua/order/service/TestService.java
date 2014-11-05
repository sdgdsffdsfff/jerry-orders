package com.hehua.order.service;

import com.hehua.order.constants.Host;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuweiwei on 14-7-28.
 */
public class TestService {

    ConfigurableApplicationContext context;

    public TestService() {
        context = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext-*.xml");
    }

    public void daemon(String[] args) {
        /*
        HehuarenService s = (HehuarenService) context.getBean("hehuarenService");
        if (args.length >= 1) {
            HehuarenModel m = s.getById(Integer.parseInt(args[0]));
            System.out.println("id=" + m.getId() + ",name=" + m.getName() + ",counter="
                    + m.getCounter());
        } else {
            System.out.println("args not exists, args length:" + args.length);
        }

        OrdersService o = (OrdersService)context.getBean("ordersService");
        OrdersServiceCreateOrderParam info = new OrdersServiceCreateOrderParam();
        info.setIp("127.0.0.1");
        info.setMobile(13141097831L);
        ArrayList<OrderItemsModel> items = new ArrayList<OrderItemsModel>();
        OrderItemsModel item = new OrderItemsModel();
        item.setQuantity(1);
        item.setItemtype("2,3");
        item.setItemid(10000);
        items.add(item);
        info.setItems(items);
        JSONObject ret = o.updateCreateOrder(info);
        System.out.println(ret);
        */

        /*
        CartService c = (CartService)context.getBean("cartService");
        CartServiceAdd2CartParam info = new CartServiceAdd2CartParam();
        ArrayList<CartModel> carts = new ArrayList<CartModel>();
        CartModel cart = new CartModel();
        cart.setItemid(89);
        cart.setItemtype("4,5");
        cart.setQuantity(1);
        carts.add(cart);
        info.setItems(carts);
        JSONObject ret = c.updateAdd2Cart(info);

        PayService p = (PayService) context.getBean("payService");
        PayServicePayParam info = new PayServicePayParam();
        info.setOrderid(10);
        info.setPaytype("alipaymobile");
        JSONObject ret = p.pay(info);
        System.out.println(ret);

        NotifyService notifyService = (NotifyService)context.getBean("notifyService");
        String str = "discount=0.00&payment_type=1&subject=%E8%8D%B7%E8%8A%B1%E4%BA%B2%E5%AD%901%E7%A7%8D%E5%95%86%E5%93%81&trade_no=2014080639969309&buyer_email=youly001%40gmail.com&gmt_create=2014-08-06+00%3A28%3A05&notify_type=trade_status_sync&quantity=1&out_trade_no=b-49-1407251312&seller_id=2088011039154714&notify_time=2014-08-06+00%3A28%3A05&body=%E8%8D%B7%E8%8A%B1%E4%BA%B2%E5%AD%901%E7%A7%8D%E5%95%86%E5%93%81&trade_status=TRADE_FINISHED&is_total_fee_adjust=N&total_fee=0.01&gmt_payment=2014-08-06+00%3A28%3A05&seller_email=yingyufang1%40126.com&gmt_close=2014-08-06+00%3A28%3A05&price=0.01&buyer_id=2088902240384099&notify_id=0ca40e34d4c6cbc38b6d99398fd0fb502i&use_coupon=N&sign_type=RSA&sign=MBs4euXDrty6WfscjHtUH6b1Sd5Pob8JRleJbE%2BW1Wv9%2B5MPFWnp2TzUTRWU4QPZgRhHvXd6lDNttgVflEhQIo9Z7jmD2ffTnB2AmFTEnwAmxEDNl3y%2BJrUSBngkIUVanXlMNzSBJ5Rqo6Ihu6WnYZSZgO%2FJ1VjP2Q%2FegVgOr%2BU%3D";

        try {
            str = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        boolean r = notifyService.saveNotify("alipaymobile", str);
        System.out.println(r);

        try {
            MyHttpClient myHttpClient = new MyHttpClient();
            String resp = myHttpClient.httpGet("http://api.s.hehua.com/orders/detail/40", "utf-8");
            System.out.println(resp);
            String postParams = "{\"items\":[{\"itemid\":1,\"type\":\"1\",\"quantity\":1},{\"itemid\":2,\"type\":\"1,2\",\"quantity\":2}]}";
            String resp2 = myHttpClient.httpPostJson("http://api.s.hehua.com/orders/confirm", postParams);
            System.out.println(resp2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setDeliveryNum("5735417469");
        deliveryInfo.setDeliveryComPinyin("yuantong");
        deliveryInfo.setDeliveryCompany("圆通快递");
        DeliveryService deliveryService = (DeliveryService)context.getBean("deliveryService");
        deliveryService.query(deliveryInfo);
        System.out.println(JSON.toJSON(deliveryInfo));
        AddressService addressService = (AddressService)context.getBean("addressService");
        AddressInfo addressInfo = deliveryService.getAddressInfo(addressService.getDefaultAddress(123456));
        System.out.println(JSON.toJSON(addressInfo));


/*
        String data = "{\"total\":96,\"rows\":[{\"_ExpressID\":1,\"_ExpressIndex\":\"A\",\"_ExpressKey\":\"aae\",\"_ExpressName\":\"AAE快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-610-0400\",\"_ExpressWebsite\":\"http:\\/\\/cn.aaeweb.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":2,\"_ExpressIndex\":\"A\",\"_ExpressKey\":\"axd\",\"_ExpressName\":\"安信达快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-54224681\",\"_ExpressWebsite\":\"http:\\/\\/www.anxinda.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":3,\"_ExpressIndex\":\"A\",\"_ExpressKey\":\"aj\",\"_ExpressName\":\"安捷快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-056-5656\",\"_ExpressWebsite\":\"http:\\/\\/www.anjelex.com\\/index.asp\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":5,\"_ExpressIndex\":\"B\",\"_ExpressKey\":\"bfdf\",\"_ExpressName\":\"百福东方\",\"_ExpressState\":1,\"_ExpressTelephone\":\"010-57169000\",\"_ExpressWebsite\":\"http:\\/\\/www.ees.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":6,\"_ExpressIndex\":\"B\",\"_ExpressKey\":\"bgpyghx\",\"_ExpressName\":\"包裹、平邮、挂号信\",\"_ExpressState\":1,\"_ExpressTelephone\":\"11185\",\"_ExpressWebsite\":\"http:\\/\\/yjcx.chinapost.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":7,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"cxwl\",\"_ExpressName\":\"传喜物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-777-5656\",\"_ExpressWebsite\":\"http:\\/\\/www.cxcod.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":8,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"chengguang\",\"_ExpressName\":\"程光快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0064 9 948 2780\",\"_ExpressWebsite\":\"http:\\/\\/www.flyway.co.nz\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":9,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"coe\",\"_ExpressName\":\"东方快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0755-83575000\",\"_ExpressWebsite\":\"http:\\/\\/www.coe.com.hk\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":10,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"ctwl\",\"_ExpressName\":\"长通物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0731-6652 0111\",\"_ExpressWebsite\":\"http:\\/\\/www.hnct56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":11,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"cszx\",\"_ExpressName\":\"城市之星物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-628-5168\",\"_ExpressWebsite\":\"http:\\/\\/www.socl.net\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":12,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"cs\",\"_ExpressName\":\"城市100快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"010-52932760\",\"_ExpressWebsite\":\"http:\\/\\/www.bjcs100.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":13,\"_ExpressIndex\":\"C\",\"_ExpressKey\":\"chuanzhi\",\"_ExpressName\":\"传志快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"上海(021-56325555 56625555) 北京(010-67035843) 广州(020-86380996)\",\"_ExpressWebsite\":\"http:\\/\\/www.chuanzhi.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":14,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"debang\",\"_ExpressName\":\"德邦物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-830-5555\",\"_ExpressWebsite\":\"http:\\/\\/www.deppon.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":15,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"dhl\",\"_ExpressName\":\"DHL快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"800-810-8000\",\"_ExpressWebsite\":\"http:\\/\\/www.cn.dhl.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":16,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"dpex\",\"_ExpressName\":\"DPEX快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-64659883\",\"_ExpressWebsite\":\"http:\\/\\/www.szdpex.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":17,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"dsf\",\"_ExpressName\":\"递四方速递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0755-33933895\",\"_ExpressWebsite\":\"http:\\/\\/www.4px.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":18,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"dtwl\",\"_ExpressName\":\"大田物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"010-59237777\",\"_ExpressWebsite\":\"http:\\/\\/www.dtw.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":19,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"dywl\",\"_ExpressName\":\"大洋物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-0088\",\"_ExpressWebsite\":\"http:\\/\\/www.dayang365.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":20,\"_ExpressIndex\":\"D\",\"_ExpressKey\":\"ds\",\"_ExpressName\":\"D速快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0531-88636363\",\"_ExpressWebsite\":\"http:\\/\\/www.d-exp.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":21,\"_ExpressIndex\":\"E\",\"_ExpressKey\":\"ems\",\"_ExpressName\":\"EMS快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"11183\",\"_ExpressWebsite\":\"http:\\/\\/www.ems.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":22,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"fedex\",\"_ExpressName\":\"FEDEX国际快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-886-1888\",\"_ExpressWebsite\":\"http:\\/\\/fedex.com\\/cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":23,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"fedexcn\",\"_ExpressName\":\"FEDEX国内快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-886-1888\",\"_ExpressWebsite\":\"http:\\/\\/cndxp.apac.fedex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":24,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"fkd\",\"_ExpressName\":\"飞康达快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"010-84223376,010-84223378\",\"_ExpressWebsite\":\"http:\\/\\/www.fkd.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":25,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"fbwl\",\"_ExpressName\":\"飞邦物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-89066698\",\"_ExpressWebsite\":\"http:\\/\\/121.14.118.50:8050\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":26,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"feibao\",\"_ExpressName\":\"飞豹快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-000-5566\",\"_ExpressWebsite\":\"http:\\/\\/www.ztky.com\\/feibao\\/KJCX.aspx\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":27,\"_ExpressIndex\":\"F\",\"_ExpressKey\":\"feihu\",\"_ExpressName\":\"飞狐快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"010-51389299\",\"_ExpressWebsite\":\"http:\\/\\/www.feihukuaidi.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":28,\"_ExpressIndex\":\"G\",\"_ExpressKey\":\"gsdwl\",\"_ExpressName\":\"共速达物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-111-0005\",\"_ExpressWebsite\":\"http:\\/\\/www.gongsuda.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":29,\"_ExpressIndex\":\"G\",\"_ExpressKey\":\"guotong\",\"_ExpressName\":\"国通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4006-773-777\",\"_ExpressWebsite\":\"http:\\/\\/www.gto365.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":30,\"_ExpressIndex\":\"G\",\"_ExpressKey\":\"gznd\",\"_ExpressName\":\"港中能达\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-620-1111\",\"_ExpressWebsite\":\"http:\\/\\/www.nd56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":34,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"henglu\",\"_ExpressName\":\"恒路物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-182-6666\",\"_ExpressWebsite\":\"http:\\/\\/www.e-henglu.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":35,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"huiqiang\",\"_ExpressName\":\"汇强快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-000-0177\",\"_ExpressWebsite\":\"http:\\/\\/www.hq-ex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":36,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"hxlwl\",\"_ExpressName\":\"华夏龙物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0755-61211999\",\"_ExpressWebsite\":\"http:\\/\\/www.chinadragon56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":37,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"hswl\",\"_ExpressName\":\"昊盛物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-186-5566\",\"_ExpressWebsite\":\"http:\\/\\/www.hs-express.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":32,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"huitong\",\"_ExpressName\":\"汇通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-62963636\",\"_ExpressWebsite\":\"http:\\/\\/www.htky365.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":63,\"_ExpressIndex\":\"H\",\"_ExpressKey\":\"haihong\",\"_ExpressName\":\"山东海红快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-632-9988\",\"_ExpressWebsite\":\"http:\\/\\/www.haihongwangsong.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":95,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jingdong\",\"_ExpressName\":\"京东快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-603-3600\",\"_ExpressWebsite\":\"http:\\/\\/jd-ex.com\\/\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":38,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jldt\",\"_ExpressName\":\"嘉里大通物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-610-3188\",\"_ExpressWebsite\":\"http:\\/\\/www.kerryeas.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":39,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jywl\",\"_ExpressName\":\"佳怡物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-660-5656\",\"_ExpressWebsite\":\"http:\\/\\/www.jiayi56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":40,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jiaji\",\"_ExpressName\":\"佳吉快运\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-5566\",\"_ExpressWebsite\":\"http:\\/\\/www.jiaji.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":41,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jiayunmei\",\"_ExpressName\":\"加运美快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-8551-5555\",\"_ExpressWebsite\":\"http:\\/\\/www.tms56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":42,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jingguang\",\"_ExpressName\":\"京广快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-83660666-60\",\"_ExpressWebsite\":\"http:\\/\\/www.kke.com.hk\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":43,\"_ExpressIndex\":\"J\",\"_ExpressKey\":\"jinyue\",\"_ExpressName\":\"晋越快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-85158039\",\"_ExpressWebsite\":\"http:\\/\\/www.byondex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":44,\"_ExpressIndex\":\"K\",\"_ExpressKey\":\"kuaijie\",\"_ExpressName\":\"快捷快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-830-4888\",\"_ExpressWebsite\":\"http:\\/\\/www.fastexpress.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":45,\"_ExpressIndex\":\"K\",\"_ExpressKey\":\"klwl\",\"_ExpressName\":\"康力物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-156-5156\",\"_ExpressWebsite\":\"http:\\/\\/www.kangliex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":46,\"_ExpressIndex\":\"L\",\"_ExpressKey\":\"longbang\",\"_ExpressName\":\"龙邦快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-39283333\",\"_ExpressWebsite\":\"http:\\/\\/www.lbex.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":47,\"_ExpressIndex\":\"L\",\"_ExpressKey\":\"lianhaotong\",\"_ExpressName\":\"联昊通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-88620000\",\"_ExpressWebsite\":\"http:\\/\\/www.lhtex.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":48,\"_ExpressIndex\":\"L\",\"_ExpressKey\":\"lejiedi\",\"_ExpressName\":\"乐捷递快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-618-1400\",\"_ExpressWebsite\":\"http:\\/\\/www.ljd360.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":49,\"_ExpressIndex\":\"L\",\"_ExpressKey\":\"lijisong\",\"_ExpressName\":\"立即送\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-028-5666\",\"_ExpressWebsite\":\"http:\\/\\/www.cdljs.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":50,\"_ExpressIndex\":\"M\",\"_ExpressKey\":\"minbang\",\"_ExpressName\":\"民邦快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-81515303\",\"_ExpressWebsite\":\"http:\\/\\/www.ywfex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":51,\"_ExpressIndex\":\"M\",\"_ExpressKey\":\"minhang\",\"_ExpressName\":\"民航快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-817-4008\",\"_ExpressWebsite\":\"http:\\/\\/www.cae.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":52,\"_ExpressIndex\":\"M\",\"_ExpressKey\":\"meiguo\",\"_ExpressName\":\"美国快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"888-611-1888\",\"_ExpressWebsite\":\"http:\\/\\/www.us-ex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":53,\"_ExpressIndex\":\"O\",\"_ExpressKey\":\"ocs\",\"_ExpressName\":\"OCS快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-118-8588\",\"_ExpressWebsite\":\"http:\\/\\/www.ocschina.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":54,\"_ExpressIndex\":\"P\",\"_ExpressKey\":\"pinganda\",\"_ExpressName\":\"平安达快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4006-230-009\",\"_ExpressWebsite\":\"http:\\/\\/www.padtf.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":55,\"_ExpressIndex\":\"Q\",\"_ExpressKey\":\"quanfeng\",\"_ExpressName\":\"全峰快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-100-0001\",\"_ExpressWebsite\":\"http:\\/\\/www.qfkd.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":56,\"_ExpressIndex\":\"Q\",\"_ExpressKey\":\"quanyi\",\"_ExpressName\":\"全一快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-663-1111\",\"_ExpressWebsite\":\"http:\\/\\/www.unitop-apex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":57,\"_ExpressIndex\":\"Q\",\"_ExpressKey\":\"quanchen\",\"_ExpressName\":\"全晨快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-82026703\",\"_ExpressWebsite\":\"http:\\/\\/www.qckd.net\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":58,\"_ExpressIndex\":\"Q\",\"_ExpressKey\":\"quanritong\",\"_ExpressName\":\"全日通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"020-86298999\",\"_ExpressWebsite\":\"http:\\/\\/www.at-express.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":59,\"_ExpressIndex\":\"R\",\"_ExpressKey\":\"rufengda\",\"_ExpressName\":\"如风达快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-010-6660\",\"_ExpressWebsite\":\"http:\\/\\/www.rufengda.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":60,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"shentong\",\"_ExpressName\":\"申通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-889-5543\",\"_ExpressWebsite\":\"http:\\/\\/www.sto.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":61,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"shunfeng\",\"_ExpressName\":\"顺丰快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-811-1111\",\"_ExpressWebsite\":\"http:\\/\\/www.sf-express.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":62,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"suer\",\"_ExpressName\":\"速尔快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-882-2168\",\"_ExpressWebsite\":\"http:\\/\\/www.sure56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":64,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"santai\",\"_ExpressName\":\"三态速递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-881-8106\",\"_ExpressWebsite\":\"http:\\/\\/www.sfcservice.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":65,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"shenghui\",\"_ExpressName\":\"盛辉物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4008-222-222\",\"_ExpressWebsite\":\"http:\\/\\/www.shenghui56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":66,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"shengfeng\",\"_ExpressName\":\"盛丰物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0591-83621111\",\"_ExpressWebsite\":\"http:\\/\\/www.sfwl.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":67,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"shengan\",\"_ExpressName\":\"圣安物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4006-618-169\",\"_ExpressWebsite\":\"http:\\/\\/www.sa56.net\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":68,\"_ExpressIndex\":\"S\",\"_ExpressKey\":\"saiaodi\",\"_ExpressName\":\"赛澳递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4000-345-888\",\"_ExpressWebsite\":\"http:\\/\\/www.51cod.com \",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":69,\"_ExpressIndex\":\"T\",\"_ExpressKey\":\"tnt\",\"_ExpressName\":\"TNT快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"800-820-9868\",\"_ExpressWebsite\":\"http:\\/\\/www.tnt.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":70,\"_ExpressIndex\":\"T\",\"_ExpressKey\":\"thtx\",\"_ExpressName\":\"通和天下物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-0056-516\",\"_ExpressWebsite\":\"http:\\/\\/www.cod56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":71,\"_ExpressIndex\":\"T\",\"_ExpressKey\":\"tcwl\",\"_ExpressName\":\"通成物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-36161199-8021\",\"_ExpressWebsite\":\"http:\\/\\/www.tc56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":33,\"_ExpressIndex\":\"T\",\"_ExpressKey\":\"tiantian\",\"_ExpressName\":\"天天快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-8198\",\"_ExpressWebsite\":\"http:\\/\\/www.ttkdex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":31,\"_ExpressIndex\":\"T\",\"_ExpressKey\":\"tdhy\",\"_ExpressName\":\"天地华宇\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-808-6666\",\"_ExpressWebsite\":\"http:\\/\\/www.hoau.net\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":72,\"_ExpressIndex\":\"U\",\"_ExpressKey\":\"ups\",\"_ExpressName\":\"UPS国际快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-8388\",\"_ExpressWebsite\":\"http:\\/\\/www.ups.com\\/cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":74,\"_ExpressIndex\":\"W\",\"_ExpressKey\":\"weibang\",\"_ExpressName\":\"伟邦快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-64212098\\/021-62629520\",\"_ExpressWebsite\":\"http:\\/\\/www.scsexpress.com\\/?char_set=gb\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":75,\"_ExpressIndex\":\"W\",\"_ExpressKey\":\"weitepai\",\"_ExpressName\":\"微特派快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-606-0909\",\"_ExpressWebsite\":\"http:\\/\\/www.vtepai.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":76,\"_ExpressIndex\":\"W\",\"_ExpressKey\":\"wxwl\",\"_ExpressName\":\"万象物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-8088\",\"_ExpressWebsite\":\"http:\\/\\/www.ewinshine.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":77,\"_ExpressIndex\":\"X\",\"_ExpressKey\":\"xfwl\",\"_ExpressName\":\"信丰物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-830-6333\",\"_ExpressWebsite\":\"http:\\/\\/www.xf-express.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":4,\"_ExpressIndex\":\"X\",\"_ExpressKey\":\"xindan\",\"_ExpressName\":\"新蛋物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-4400\",\"_ExpressWebsite\":\"http:\\/\\/www.ozzo.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":73,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yousu\",\"_ExpressName\":\"优速快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-1111-119\",\"_ExpressWebsite\":\"http:\\/\\/www.uc56.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":78,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"xinbang\",\"_ExpressName\":\"新邦物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4008-000-222\",\"_ExpressWebsite\":\"http:\\/\\/www.xbwl.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":79,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yuantong\",\"_ExpressName\":\"圆通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-69777888\\/999\",\"_ExpressWebsite\":\"http:\\/\\/www.yto.net.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":80,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yunda\",\"_ExpressName\":\"韵达快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-821-6789\",\"_ExpressWebsite\":\"http:\\/\\/www.yundaex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":81,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yibang\",\"_ExpressName\":\"一邦快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4008-000-666\",\"_ExpressWebsite\":\"http:\\/\\/www.ebon-express.com\\/\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":82,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yuntong\",\"_ExpressName\":\"运通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0769-81156999\",\"_ExpressWebsite\":\"http:\\/\\/www.ytkd168.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":83,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yzjc\",\"_ExpressName\":\"元智捷诚快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-081-2345\",\"_ExpressWebsite\":\"http:\\/\\/www.yjkd.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":84,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yuanfeihang\",\"_ExpressName\":\"原飞航快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0755-29778899\",\"_ExpressWebsite\":\"http:\\/\\/www.yfhex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":85,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yafeng\",\"_ExpressName\":\"亚风快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-628-0018\",\"_ExpressWebsite\":\"http:\\/\\/www.broad-asia.net\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":86,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"ycwl\",\"_ExpressName\":\"远成物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-820-1646\",\"_ExpressWebsite\":\"http:\\/\\/www.ycgwl.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":87,\"_ExpressIndex\":\"Y\",\"_ExpressKey\":\"yuefeng\",\"_ExpressName\":\"越丰快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"852-23909969\",\"_ExpressWebsite\":\"http:\\/\\/www.yfexpress.com.hk\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":88,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zhongtong\",\"_ExpressName\":\"中通快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"021-39777777\",\"_ExpressWebsite\":\"http:\\/\\/www.zto.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":89,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zjs\",\"_ExpressName\":\"宅急送快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-6789-000\",\"_ExpressWebsite\":\"http:\\/\\/www.zjs.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":90,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zhongtie\",\"_ExpressName\":\"中铁快运\",\"_ExpressState\":1,\"_ExpressTelephone\":\"400-000-5566\",\"_ExpressWebsite\":\"http:\\/\\/www.ztky.com \",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":91,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zhongyou\",\"_ExpressName\":\"中邮物流\",\"_ExpressState\":1,\"_ExpressTelephone\":\"11183\",\"_ExpressWebsite\":\"http:\\/\\/www.cnpl.com.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":92,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zmkm\",\"_ExpressName\":\"芝麻开门\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4001-056-056,88056056\",\"_ExpressWebsite\":\"http:\\/\\/www.zmkmex.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":93,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zzjh\",\"_ExpressName\":\"郑州建华快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0371-65995266\",\"_ExpressWebsite\":\"http:\\/\\/www.zzjhtd.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":94,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"ztwy\",\"_ExpressName\":\"中天万运快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"0531-68850629\",\"_ExpressWebsite\":\"http:\\/\\/www.wanyun56.cn\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0},{\"_ExpressID\":96,\"_ExpressIndex\":\"Z\",\"_ExpressKey\":\"zengyi\",\"_ExpressName\":\"增益快递\",\"_ExpressState\":1,\"_ExpressTelephone\":\"4008-456-789\",\"_ExpressWebsite\":\"http:\\/\\/www.zeny-express.com\",\"_AEID\":0,\"_AgentNo\":16470,\"_State\":0}]}";
        JSONObject jo = JSON.parseObject(data);
        JSONArray ja = jo.getJSONArray("rows");
        DeliveryCompanyDAO deliveryCompanyDAO = (DeliveryCompanyDAO)context.getBean("deliveryCompanyDAO");
        for (int i = 0; i< ja.size(); i++) {
            JSONObject m = ja.getJSONObject(i);
            DeliveryCompanyModel d = new DeliveryCompanyModel();
            d.setId(Integer.parseInt(m.getString("_ExpressID")));
            d.setName(m.getString("_ExpressName"));
            d.setCode(m.getString("_ExpressKey"));
            d.setPhone(m.getString("_ExpressTelephone"));
            d.setWebsite(m.getString("_ExpressWebsite"));
            d.setStatus(0);
            deliveryCompanyDAO.add(d);
        }

        OrdersDAO ordersDAO = (OrdersDAO)context.getBean("ordersDAO");
        List<OrderStatusCountInfo> orderStatusCountInfos = ordersDAO.getCountByStatus(123456L);
        for (OrderStatusCountInfo orderStatusCountInfo : orderStatusCountInfos) {
            System.out.println(orderStatusCountInfo.getCount() + " " + orderStatusCountInfo.getStatus());
        }

        String strTypes = "5,1,6,2,3,4";
        System.out.println(StringUtils.sortIntString(strTypes));

        PayManager payManager = (PayManager)context.getBean("payManager");
        BasePayProvider basePayProvider = null;
        try {
            basePayProvider = payManager.getPayObj(1);
        } catch (NoSuchPayConfigException e) {
            e.printStackTrace();
        }
        RefundDAO refundDAO = (RefundDAO)context.getBean("refundDAO");
        basePayProvider.refund(refundDAO.getById(1));

        String code = "100109";
        Pattern postCodePattern = Pattern.compile("^[0-9]{6}$");
        boolean b = ValidationUtils.isValidPostCode(code);
        System.out.println(postCodePattern.matcher(code).matches());

        DataSourceTransactionManager txManager = (DataSourceTransactionManager)context.getBean("itemTransactionManager");
        DefaultTransactionDefinition txdef = new DefaultTransactionDefinition();
        txdef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus txStatus = txManager.getTransaction(txdef);
        ItemSkuService itemSkuService = (ItemSkuService) context.getBean("itemSkuService");
        itemSkuService.incrSkuQuantity(57, 1);
        if (true) {
            throw new RuntimeException("test");
        }
        itemSkuService.incrSkuQuantity(56, 1);
        txManager.commit(txStatus);
        System.out.println("ok");

        OrdersDAO ordersDAO = (OrdersDAO) context.getBean("ordersDAO");
        OrdersModel order = ordersDAO.getById(16);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(order.getOrderDate()));

        NotifySuccessLogDAO notifySuccessLogDAO = (NotifySuccessLogDAO)context.getBean("notifySuccessLogDAO");
        NotifySuccessLogModel notify = notifySuccessLogDAO.getByOutnoAndPaytype("b-13-1409105315", 1);
        RefundService refundService = (RefundService)context.getBean("refundService");
        refundService.updatePayFailApply(notify);

        FeedbackService feedbackService = (FeedbackService)context.getBean("feedbackService");
        System.out.println(feedbackService.getCountByItemId(72));

        RefundService refundService = (RefundService)context.getBean("refundService");
        RefundDAO refundDAO = (RefundDAO)context.getBean("refundDAO");

        RefundModel refund = refundDAO.getById(10);
        refundService.updateSendRefundToPaiu(refund);

        JmsApi jmsApi = (JmsApi)context.getBean("jmsApi");
        jmsApi.call("SyncOrderToPaiu", 40L, 20*1000);

        Logger flume = Logger.getLogger("flumeEventLogger");
        int i = 0;
        while(true) {
            flume.info("i am a test");
            if (i++ == 1000) {
                break;
            }

            //System.err.println("\n" + i + "\n");
        }
        */

        System.out.println(Host.getApiHost());
    }

    public static void main(String[] args) {
        TestService s = new TestService();
        String[] myArgs = { "2" };
        s.daemon(myArgs);
    }
}
