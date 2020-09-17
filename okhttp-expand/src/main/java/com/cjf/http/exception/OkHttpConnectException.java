package com.cjf.http.exception;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: NetworkError </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:04
 */
public class OkHttpConnectException extends OkHttpException {

    public OkHttpConnectException(String message, Response response) {
        super(message, response);
    }

    public OkHttpConnectException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpConnectException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpConnectException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpConnectException(String message, Request request) {
        super(message, request);
    }

    public OkHttpConnectException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
