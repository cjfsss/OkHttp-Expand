package com.cjf.http.exception;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpStatusCodeException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/20 15:22
 */
public class OkHttpStatusCodeException extends OkHttpIOException {

    public OkHttpStatusCodeException(String message, Response response) {
        super(message, response);
    }

    public OkHttpStatusCodeException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpStatusCodeException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpStatusCodeException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpStatusCodeException(String message, Request request) {
        super(message, request);
    }

    public OkHttpStatusCodeException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
