package com.cjf.http.exception;

import androidx.annotation.Keep;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: SocketClosedException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:04
 */
public class OkHttpSocketClosedException extends OkHttpIOException {


    public OkHttpSocketClosedException(String message, Response response) {
        super(message, response);
    }

    public OkHttpSocketClosedException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpSocketClosedException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpSocketClosedException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpSocketClosedException(String message, Request request) {
        super(message, request);
    }

    public OkHttpSocketClosedException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
