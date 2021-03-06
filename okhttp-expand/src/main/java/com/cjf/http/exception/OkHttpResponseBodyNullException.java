package com.cjf.http.exception;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpNullException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/6/5 22:18
 */
public class OkHttpResponseBodyNullException extends OkHttpException {

    public OkHttpResponseBodyNullException(String message, Response response) {
        super(message, response);
    }

    public OkHttpResponseBodyNullException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpResponseBodyNullException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpResponseBodyNullException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpResponseBodyNullException(String message, Request request) {
        super(message, request);
    }

    public OkHttpResponseBodyNullException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
