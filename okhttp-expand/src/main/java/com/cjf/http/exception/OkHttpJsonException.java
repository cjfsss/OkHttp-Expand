package com.cjf.http.exception;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: JsonSyntaxException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/29 16:08
 */
public class OkHttpJsonException extends OkHttpIOException {


    public OkHttpJsonException(String message, Response response) {
        super(message, response);
    }

    public OkHttpJsonException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpJsonException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpJsonException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpJsonException(String message, Request request) {
        super(message, request);
    }

    public OkHttpJsonException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
