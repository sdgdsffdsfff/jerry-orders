package com.hehua.order.pay;

import com.hehua.commons.time.DateUtils;
import com.hehua.order.alipay.Rsa;
import com.hehua.order.constants.Host;
import com.hehua.order.dao.NotifyDAO;
import com.hehua.order.model.NotifyModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.model.RefundModel;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuweiwei on 14-7-28.
 * 支付宝手机快捷支付处理器
 */
@Named
public final class PayAlipayMobileProvider extends BasePayProvider {

    //public final String hehua_order_private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANW7Z7H65JpjX2Sal4v5vm+tEUcHLNIZPlKA94dYwGbCWX3Wh8/8kMGtKysX2sFrZXp7PihzHE8J7Dk6W1EOUMW8rH2BQwOC6cCSz7aFujyEvWmRp71uU0GBvLuc1Iyxq+AucuFBWUNVM/2Fpbtfk24QW9JNQyt6i1NkJsgvBI/vAgMBAAECgYAiUWwOBOYN8CTSB4yL2jb8wYJDsGA+//Sk1LZqEhMarNwnu3mlKL2pi3+h9LuYUQFDUGSjpBAIlORMMBWXhz/qpPzrMSpFO9cJAAoneTG5pPxqu4HAJfNHGYeaXvJNTpAzryfuCFD8pojzys9iAToqM6xPCbudAquW+8Z/PoK4iQJBAPKVDgQKri7Dt6nO1efZytbSpJUaqcNIPtzlGDhhK+Xg4VJJ3GBsbBpcsTQ4jboTMJN6LU5lORsH1wuUamvaOpsCQQDhjdV3+nVXWnDDXWIORAVnafxVdPn8Q0Ex7PndJe/ezgfNyr6puaf5C1nyhR8I+P4FWOGkt5aaLWYHVHnoxts9AkBjixDGTc+uNvgOAWc7jd6zipqo6NkS3nmVUZk/wXgvC7nKd66sDrHJm5HH2REncypyyib/TYBCfFQdV1jYSyPTAkBfupnaRROeUoU/dZfTZC1C+emdv3sGBOlbri9SG/LVhm4repw3XtOa0KWJV9SXM05kqJeEGGMz5obCH6lqEVQlAkBLvUVg/5AfMFFh5d9z7FMDhgQ52zBs9thqLB7/om68TJflI3ZhFmR8LblSnZW/rajt9VUplgeNaqEJ5luqojS7";
    public final String hehua_order_private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKkecpCU483TcdOOGPdXzDxiykC4gQNgVZ77yXXItZFYWswmoQ4gqLdS3MdGVSJnHNDKYIZWxeNCWXXvu8DZGdw2rwwgdE2F/cMLN1UJ8mevIiKi2H2N7F7mbbRtRI5AZArjptjM2T34QbNEi+5C5DTBa5JOLLt6lREopS8tQsRxAgMBAAECgYBHl/wn+N/3Ymvtg1pOW3y6k5fLjlAPfdjBVRYUJsO2pBDMHA5STMEf2pRcSAoCZmRsf0Adnt5nNs9offSdz0qCnt7GNobcKcYVc36qXSmxDyuc9Pb1+zt2W8sneaHw8lwJSEN7/2pDOZQO7IdOXDAhYi1HR80aaLY2EpUqbrKtgQJBANchspuCY6kbGyTYaWjCgLtMeo6QISEk7jnpV7ibyRPcj/QQ7FLtDUYKLlKpjj30eqQ2VIep7S7X3Q3JVoVojKcCQQDJPw5S7G0a3x6oeTgksWUZXV6NKPYX4BShSGRWrRoGUcf2QlKdAIoguoqMsksF1g+S4sX/A9GjR/39OlxPl9EnAkEAuXGd0blTCVmJS3iVXK2VZF7nom6RcE2yYQ9JA6Seb1WIbX4NCOq4r3BlP/JS4AsXkOgYc51CDEgdkB3HTFa0LwJARZ0ISWr7OsZfhouX/ilW/XUs1cuLhSrAOCIgg1MRo4n6j5bVmPWs0jF058Js7THk5TyInPQnOkIW+9eKQwYTFQJAcAGJ0sV48ziq0WPY1HpkrgMCJq7FJMS6bVyglULmwRMiRw99a+OpL8Y3GSo3p5KD8nmFjoNLLzVUc+83qHufcg==";
    public final String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    public final String alipay_service_gateway = "https://mapi.alipay.com/gateway.do";
    public final String service = "mobile.securitypay.pay";
    //public final String partner = "2088011039154714";
    public final String partner = "2088512939608254";
    public final String input_charset = "utf-8";
    public final String sign_type = "RSA";
    public final String payment_type = "1";
    //public final String seller_id = "yingyufang1@126.com";
    public final String seller_id = "i@hehuababy.com";
    public final String it_b_pay = "2h";


    public final String refund_service = "refund_fastpay_by_platform_pwd";

    @Inject
    NotifyDAO notifyDAO;

    @Override
    public int getPayType() {
        return 1;
    }

    private String getPayNotifyUrl() {
        return "http://" + Host.getApiHost() + "/orders/notify/alipaymobile";
    }

    private String getRefundNotifyUrl() {
        return "http://" + Host.getApiHost() + "/refund/notify/alipaymobile";
    }

    @Override
    public String getPayUrl(OrdersModel order) {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("service", this.service);
        param.put("partner", this.partner);
        param.put("_input_charset", this.input_charset);
        try {
            param.put("notify_url", URLEncoder.encode(this.getPayNotifyUrl(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("url encode error", e);
            return "fail";
        }
        param.put("payment_type", this.payment_type);
        param.put("seller_id", this.seller_id);

        BigDecimal needToPay = order.getTotalfee().subtract(order.getPayed());
        param.put("total_fee", needToPay.toString());

        String out_trade_no =  OrdersModel.genOutno(order, 1);
        param.put("out_trade_no", out_trade_no);

        String subject = "荷花亲子" + order.getOrderitems().size() + "种商品";
        param.put("subject", subject);
        param.put("body", subject);
        param.put("it_b_pay", this.it_b_pay);

        String strToSign = this.map2url(param);

        if (log.isDebugEnabled()) {
            System.out.println("\nstr_to_sign:" + strToSign + "\n");
        }

        String sign = Rsa.sign(strToSign, this.hehua_order_private_key);
        try {
            sign = URLEncoder.encode(sign, "utf8");
        } catch (UnsupportedEncodingException e) {
            log.error("sign encode error", e);
            return "fail";
        }

        String payUrl = strToSign + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";

        if (log.isDebugEnabled()) {
            System.out.println("\npayUrl:" + payUrl);
        }

        return payUrl;
    }

    @Override
    public boolean verifySign(String content) {
        Map<String, String> urlmap = this.url2map(content);
        if (urlmap.get("sign_type") == null || !urlmap.get("sign_type").equalsIgnoreCase("rsa")) {
            return false;
        }
        String sign = urlmap.get("sign");

        if (log.isDebugEnabled()) {
            System.out.println("sign:" + sign);
        }
        urlmap.remove("sign");
        urlmap.remove("sign_type");

        String strToVerify = this.prepareSignStr(urlmap, false);

        if (log.isDebugEnabled()) {
            System.out.println("strToVerify" + strToVerify);
        }
        return Rsa.doCheck(strToVerify, sign, this.alipay_public_key);
    }

    /**
     * 验证同步返回的支付结果是否成功
     * @param content
     * @return
     */
    public boolean checkSyncResult(String content) {

        log.info("sync result:" + content);
        HashMap<String, String> ret = new HashMap<String, String>();
        String resultStatus = this.getContent(content, "resultStatus={", "}");
        if (resultStatus.equals("") || !resultStatus.equals("9000")) {
            return false;
        }
        String result = this.getContent(content, "result={", "}");
        if (result == null || result.equals("")) {
            return false;
        }
        String sign_type = this.getContent(result, "sign_type=\"", "\"");
        if (sign_type == null || sign_type.equals("") || !sign_type.equalsIgnoreCase("rsa")) {
            return false;
        }
        String sign = this.getContent(result, "sign=\"", "\"");
        String strToVerify = content.replaceAll("&sign=\"[^\"]*\"", "").replaceAll("&sign_type=\"[^\"]*\"", "");
        return Rsa.doCheck(strToVerify, sign, this.alipay_public_key);
    }

    /**
     * 保存支付宝支付异步通知结果
     * @param content 第三方通知字符串
     * @param notifyid 新产生的id
     * @return
     */
    @Override
    public boolean saveNotify(String content, NotifyId notifyid) {
        if (!this.verifySign(content)) {
            log.error("sign error");
            return false;
        }
        Map<String, String> map = this.url2map(content);
        String out_trade_no = map.get("out_trade_no");
        NotifyModel notify = new NotifyModel();
        notify.setObjid(OrdersModel.getOrderIdByOutno(out_trade_no));
        notify.setOutno(out_trade_no);
        if (OrdersModel.getOrderTypeByOutno(out_trade_no).equalsIgnoreCase("c")) {
            notify.setType(NotifyModel.TYPE_CHARGE);
        } else {
            notify.setType(NotifyModel.TYPE_PAY);
        }
        notify.setPaytype(NotifyModel.TYPE_PAY_ALIPAYMOBILE);
        if (map.get("total_fee") != null) {
            notify.setMoney(new BigDecimal(map.get("total_fee")));
        }
        if (map.get("trade_no") != null) {
            notify.setTradeno(map.get("trade_no"));
        }
        if (map.get("buyer_email") != null) {
            notify.setBuyer(map.get("buyer_email"));
        }
        if (map.get("trade_status") != null) {
            notify.setResult(map.get("trade_status"));
        }
        notify.setPaytime((int)(System.currentTimeMillis() / 1000L));
        notify.setSign(map.get("sign"));
        notify.setStatus(0);
        notify.setData(content);
        notify.setAddtime((int)(System.currentTimeMillis() / 1000L));
        notify.setModtime((int)(System.currentTimeMillis() / 1000L));
        notify.initWhenAdd();
        if (notifyDAO.add(notify) == 0) {
            log.error("sql error" + notify.toString());
            return false;
        }
        notifyid.setId(notify.getId());
        return true;
    }

    @Override
    public boolean checkTradeStatus(NotifyModel notify) {
        if (StringUtils.isBlank(notify.getResult())) {
            return false;
        }
        if (notify.getResult().equals("TRADE_SUCCESS")) {
            return true;
        }
        //交易关闭，但是三天之内通知过来，认为合法
        if (notify.getResult().equals("TRADE_FINISHED") && (notify.getAddtime() + 3*86400 > System.currentTimeMillis()/1000L)) {
            return true;
        }
        return false;
    }

    @Override
    public String refund(RefundModel refund) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("service", this.refund_service);
        param.put("partner", this.partner);
        param.put("_input_charset", this.input_charset);
        param.put("notify_url", this.getRefundNotifyUrl());
        param.put("seller_email", this.seller_id);
        param.put("seller_user_id", this.partner);
        param.put("refund_date", DateUtils.formatTimestamp(refund.getSendtime(), false));
        param.put("batch_no", refund.getOutno());
        param.put("batch_num", "1");
        String detail_data = refund.getTradeno() + "^" + refund.getMoney() + "^退交易";
        param.put("detail_data", detail_data);

        String strToSign = this.prepareSignStr(param, false);
        String sign = Rsa.sign(strToSign, this.hehua_order_private_key);
        try {
            sign = URLEncoder.encode(sign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return this.FAIL_CODE;
        }
        String refundUrl = this.alipay_service_gateway + "?" + strToSign + "&sign=" + sign + "&sign_type=RSA";
        if (log.isDebugEnabled()) {
            System.out.print(refundUrl);
        }
        return refundUrl;
    }
}
