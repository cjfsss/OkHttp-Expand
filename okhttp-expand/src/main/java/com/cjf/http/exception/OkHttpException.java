package com.cjf.http.exception;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpExecption </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/6/5 20:57
 */
public class OkHttpException extends RuntimeException {

    private String requestMethod; //请求方法，Get/Post等
    private String requestUrl; //请求Url及参数
    private Headers responseHeaders; //响应头
    private String requestResult; //请求结果

    public OkHttpException(String message, Response response) {
        this(message, response, null, null);
    }

    public OkHttpException(String message, Response response, Throwable throwable) {
        this(message, response, throwable, null);
    }

    public OkHttpException(String message, Response response, String result) {
        this(message, response, null, result);
    }

    public OkHttpException(String message, Response response, Throwable throwable, String result) {
        super(message, throwable);
        requestResult = result;
        Request request = response.request();
        requestMethod = request.method();
        requestUrl = request.url().toString();
        responseHeaders = response.headers();
    }

    public OkHttpException(String message, Request request) {
        this(message, request, null);
    }

    public OkHttpException(String message, Request request, Throwable throwable) {
        super(message, throwable);
        requestMethod = request.method();
        requestUrl = request.url().toString();
        responseHeaders = request.headers();
    }


    public String getRequestResult() {
        return requestResult;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":" + " Method=" + requestMethod + "\n\n" + requestUrl + "\n\n" +
               responseHeaders + "\nmessage = " + getMessage();
    }
}
