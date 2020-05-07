package com.cjf.okhttp_expand.exception;

import com.cjf.okhttp_expand.utils.RequestUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: ParseException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/29 16:07
 */
public class ParseException extends IOException {

    private String errorCode;

    private String requestMethod; //请求方法，Get/Post等
    private String requestUrl; //请求Url及参数
    private Headers responseHeaders; //响应头
    private String requestResult; //请求结果

    @Deprecated
    public ParseException(String message, Response response) {
        this("-1", message, response, null);
    }

    @Deprecated
    public ParseException(String message, Response response, String result) {
        this("-1", message, response, result);
    }

    public ParseException(String code, String message, Response response) {
        this(code, message, response, null);
    }

    public ParseException(String code, String message, Response response, String result) {
        super(message);
        errorCode = code;
        requestResult = result;

        Request request = response.request();
        requestMethod = request.method();
        requestUrl = RequestUtils.getEncodedUrlAndParams(request);
        responseHeaders = response.headers();
    }

    public String getRequestResult() {
        return requestResult;
    }

    public String getErrorCode() {
        return errorCode;
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
    public String getLocalizedMessage() {
        return errorCode;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":" +
                " Method=" + requestMethod +
                " Code=" + errorCode +
                "\n\n" + requestUrl +
                "\n\n" + responseHeaders +
                "\nmessage = " + getMessage();
    }
}
