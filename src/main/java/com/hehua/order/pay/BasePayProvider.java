package com.hehua.order.pay;

import java.util.*;

import com.hehua.order.info.PayTypeInfo;
import org.apache.log4j.Logger;

/**
 * Created by liuweiwei on 14-7-30.
 * 支付基础类
 */
public abstract class BasePayProvider implements PayProvider {

    static Logger log = Logger.getLogger(BasePayProvider.class.getName());

    public static final String FAIL_CODE = "fail";

    public static HashMap<Integer, String> payIdNameMap = new HashMap<Integer, String>() {
        {
            put(1, "alipaymobile");
        }
    };

    public static HashMap<Integer, String> payIdCNameMap = new HashMap<Integer, String>() {
        {
            put(1, "支付宝手机快捷支付");
        }
    };

    public static HashMap<String, Integer> payNameIdMap = new HashMap<String, Integer>() {
        {
            put("alipaymobile", 1);
        }
    };

    /**
     * 获取支持的支付方式列表
     * @return
     */
    public static List<PayTypeInfo> getSupportedPayTypes() {
        List<PayTypeInfo> payTypeInfos = new ArrayList<>();
        Iterator iter = BasePayProvider.payIdNameMap.keySet().iterator();
        while (iter.hasNext()) {
            Integer payid = (Integer)iter.next();
            PayTypeInfo payTypeInfo = new PayTypeInfo();
            payTypeInfo.setId(payid);
            payTypeInfo.setName(BasePayProvider.payIdCNameMap.get(payid));
            payTypeInfos.add(payTypeInfo);
        }
        return  payTypeInfos;
    }

    public abstract int getPayType();

    /**
     * 过滤数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 将Map拼接成 key1=value1&key2=value2 的形式
     * @param params 需要参与字符拼接的Map
     * @return
     */
    public String map2url(Map<String, String> params) {
        Iterator iter = params.entrySet().iterator();
        String str = "";
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String,String>)iter.next();
            str += entry.getKey() + "=\"" + entry.getValue() + "\"&";
        }
        return str.substring(0, str.length() -1);
    }

    /**
     * 将字符串 key1=value1&key2=value2 解析成Hashmap的形式
     * @param url
     * @return
     */
    public Map<String, String> url2map(String url) {
        url = url.replaceAll("\"", "");
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            String[] arr = url.split("&");
            for (int i = 0; i < arr.length; i++) {
                String[] keyval = arr[i].split("=");
                result.put(keyval[0], arr[i].substring(keyval[0].length() + 1));
            }
        } catch (Exception e) {
            log.error("url error", e);
        }
        return result;
    }

    /**
     * 对key进行升序排序，然后拼接成 key1=value1&key2=value2 的形式
     * @param params 需要排序并参与字符拼接的Map
     * @return
     */
    public String prepareSignStr(Map<String, String> params, boolean withQuote) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (withQuote) {
                prestr = prestr + key + "=\"" + value + "\"&";
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr.substring(0, prestr.length() -1);
    }

    /**
     * 获取字符串src中startTag与endTag之间的字符串
     * @param src 源字符串
     * @param startTag 起始标签
     * @param endTag 结束标签
     * @return
     */
    public  String getContent(String src, String startTag, String endTag) {
        String content = src;
        int start = src.indexOf(startTag);
        start += startTag.length();
        try {
            if (endTag != null) {
                int end = src.indexOf(endTag);
                content = src.substring(start, end);
            } else {
                content = src.substring(start);
            }
        } catch (Exception e) {
            log.error("substring error", e);
            return "";
        }
        return content;
    }
}