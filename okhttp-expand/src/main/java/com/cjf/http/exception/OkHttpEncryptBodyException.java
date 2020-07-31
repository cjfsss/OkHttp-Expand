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
public class OkHttpEncryptBodyException extends OkHttpException {


    public OkHttpEncryptBodyException(String message, Response response) {
        super(message, response);
    }

    public OkHttpEncryptBodyException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpEncryptBodyException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpEncryptBodyException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpEncryptBodyException(String message, Request request) {
        super(message, request);
    }

    public OkHttpEncryptBodyException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
