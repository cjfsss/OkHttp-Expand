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
public class OkHttpEncryptException extends OkHttpIOException {


    public OkHttpEncryptException(String message, Response response) {
        super(message, response);
    }

    public OkHttpEncryptException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpEncryptException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpEncryptException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpEncryptException(String message, Request request) {
        super(message, request);
    }

    public OkHttpEncryptException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
