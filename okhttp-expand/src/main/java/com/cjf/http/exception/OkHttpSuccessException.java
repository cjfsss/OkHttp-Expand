package com.cjf.http.exception;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpEncryptBodyException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/20 14:50
 */
public class OkHttpSuccessException extends OkHttpException {

    public OkHttpSuccessException(String message, Response response) {
        super(message, response);
    }

    public OkHttpSuccessException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpSuccessException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpSuccessException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpSuccessException(String message, Request request) {
        super(message, request);
    }

    public OkHttpSuccessException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
