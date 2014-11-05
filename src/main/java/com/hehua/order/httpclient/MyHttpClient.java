package com.hehua.order.httpclient;

import java.io.IOException;
import java.util.*;

import com.hehua.order.exceptions.HttpStatusCodeException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Created by liuweiwei on 14-8-12.
 */
public class MyHttpClient {

    private Logger log = Logger.getLogger(MyHttpClient.class.getName());

    private HashMap<String, String> headers = new HashMap<String, String>() {
        {
            put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
            put("Accept", "*/*");
            put("Accept-Encoding", "gzip,deflate");
            put("Accept-Language", "zh-cn");
        }
    };

    private HashMap<String, Integer> config = new HashMap<String, Integer>() {
        {
            /* 最大重定向次数 */
            put("max_redirect", 10);
            /* 连接超时，毫秒 */
            put("connect_timeout", 10000);
            /* 套接字数据传输超时 */
            put("socket_timeout", 30000);
            /* keep_alive时间设置，若为0则禁用 */
            put("keep_alive", 300);
        }
    };

    private RequestConfig.Builder requestConfigBuilder;

    public MyHttpClient() {
        this.requestConfigBuilder = RequestConfig.custom();
    }

    public MyHttpClient(Map<String, String> headers, Map<String, Integer> config) {
        Iterator headerIter = headers.entrySet().iterator();
        while (headerIter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) headerIter.next();
            this.headers.put(entry.getKey(), entry.getValue());
            if (log.isDebugEnabled()) {
                log.debug("[add header]" + entry.getKey() + ":" + entry.getValue());
            }
        }

        Iterator configIter = config.entrySet().iterator();
        while (configIter.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) configIter.next();
            this.config.put(entry.getKey(), entry.getValue());
            if (log.isDebugEnabled()) {
                log.debug("[add config]" + entry.getKey() + ":" + entry.getValue());
            }
        }
        this.requestConfigBuilder = RequestConfig.custom();
    }

    private void init(HttpRequestBase httpRequest) {
        Iterator headerIter = this.headers.entrySet().iterator();
        while (headerIter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) headerIter.next();
            httpRequest.setHeader(entry.getKey(), entry.getValue());
            if (log.isDebugEnabled()) {
                log.debug("[set header]" + entry.getKey() + ":" + entry.getValue());
            }
        }

        Iterator configIter = this.config.entrySet().iterator();
        while (configIter.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) configIter.next();
            if (entry.getKey().equalsIgnoreCase("max_redirect")) {
                this.requestConfigBuilder.setMaxRedirects(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("connect_timeout")) {
                this.requestConfigBuilder.setConnectTimeout(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("socket_timeout")) {
                this.requestConfigBuilder.setSocketTimeout(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("keep_alive")) {
                if (entry.getValue() != 0) {
                    httpRequest.setHeader("Keep-Alive", String.valueOf(entry.getValue()));
                    httpRequest.setHeader("Connection", "keep-alive");
                } else {
                    httpRequest.setHeader("Connection", "close");
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("[add config]" + entry.getKey() + ":" + entry.getValue());
            }
        }
        httpRequest.setConfig(this.requestConfigBuilder.build());
    }

    public String httpGet(String url, String encoding) throws IOException, HttpStatusCodeException {
        if (log.isDebugEnabled()) {
            System.out.println(url);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        this.init(get);
        CloseableHttpResponse response = httpclient.execute(get);
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            if (log.isDebugEnabled()) {
                log.error("url:" + url + " http code error:" + code);
            }
            throw new HttpStatusCodeException(code);
        }
        HttpEntity entity = response.getEntity();
        response.close();
        return EntityUtils.toString(entity, encoding);
    }

    public String httpGet(String url) throws IOException, HttpStatusCodeException {
        return this.httpGet(url, "utf-8");
    }

    public String httpPost(String url, String postParams, String encoding) throws IOException, HttpStatusCodeException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        this.headers.put("content-type", "application/x-www-form-urlencoded");
        this.init(post);
        post.setEntity(new StringEntity(postParams, encoding));
        CloseableHttpResponse response = httpclient.execute(post);
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            if (log.isDebugEnabled()) {
                log.error("url:" + url + " http code error:" + code);
            }
            throw new HttpStatusCodeException(code);
        }
        HttpEntity entity = response.getEntity();
        response.close();
        return EntityUtils.toString(entity, encoding);
    }

    public String httpPost(String url, String postParams) throws IOException, HttpStatusCodeException {
        return this.httpPost(url, postParams, "utf-8");
    }

    public String httpPostJson(String url, String postParams, String encoding) throws IOException, HttpStatusCodeException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        this.headers.put("content-type", "application/json");
        this.init(post);
        post.setEntity(new StringEntity(postParams));
        CloseableHttpResponse response = httpclient.execute(post);
        int code = response.getStatusLine().getStatusCode();
        if (code != 200) {
            if (log.isDebugEnabled()) {
                log.error("url:" + url + " http code error:" + code);
            }
            throw new HttpStatusCodeException(code);
        }
        HttpEntity entity = response.getEntity();
        response.close();
        return EntityUtils.toString(entity, encoding);
    }

    public String httpPostJson(String url, String postParams) throws IOException, HttpStatusCodeException {
        return this.httpPostJson(url, postParams, "utf-8");
    }
}
