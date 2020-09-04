package com.cjf.http.exception;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpEncryptBodyException </p>
 * <p>Description: 请求成功，结果处理异常 </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/20 14:50
 */
public class OkHttpSuccessExecuteException extends OkHttpException {

    public OkHttpSuccessExecuteException(String message, Response response) {
        super(message, response);
    }

    public OkHttpSuccessExecuteException(String message, Response response, Throwable throwable) {
        super(message, response, throwable);
    }

    public OkHttpSuccessExecuteException(String message, Response response, String result) {
        super(message, response, result);
    }

    public OkHttpSuccessExecuteException(String message, Response response, Throwable throwable, String result) {
        super(message, response, throwable, result);
    }

    public OkHttpSuccessExecuteException(String message, Request request) {
        super(message, request);
    }

    public OkHttpSuccessExecuteException(String message, Request request, Throwable throwable) {
        super(message, request, throwable);
    }
}
