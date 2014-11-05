package com.hehua.order.exceptions;

/**
 * Created by liuweiwei on 14-8-12.
 */
public class HttpStatusCodeException extends Exception {

    public HttpStatusCodeException(int code) {
        super("http code error:" + String.valueOf(code));
    }
}
