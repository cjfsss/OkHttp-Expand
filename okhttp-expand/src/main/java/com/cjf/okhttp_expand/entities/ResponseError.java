package com.cjf.okhttp_expand.entities;

import com.cjf.okhttp_expand.constant.HttpConstants;

import java.io.Serializable;

/**
 * Http请求错误信息
 * User: ljx
 * Date: 2019-06-26
 * Time: 14:26
 */
public class ResponseError implements IResponseError, HttpConstants, Serializable {

    private static final long serialVersionUID = 3494161099162647843L;

    private int httpCode;

    private String httpMsg;

    private int errorCode;  //仅指服务器返回的错误码

    private Throwable throwable; //异常信息

    public ResponseError(int httpCode, String httpMsg) {
        this.httpCode = httpCode;
        this.httpMsg = httpMsg;
    }

    public ResponseError(int httpCode, String httpMsg, Throwable throwable) {
        this.httpCode = httpCode;
        this.httpMsg = httpMsg;
        this.throwable = throwable;
    }

    public ResponseError(int httpCode, String httpMsg, int errorCode, Throwable throwable) {
        this.httpCode = httpCode;
        this.httpMsg = httpMsg;
        this.errorCode = errorCode;
        this.throwable = throwable;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMsg() {
        return httpMsg;
    }

    public void setHttpMsg(String httpMsg) {
        this.httpMsg = httpMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean isNoNetWork() {
        return STATUS_NO_NETWORK == getHttpCode();
    }

    @Override
    public boolean isNetWork() {
        return STATUS_NETWORK_EXCEPTION == getHttpCode();
    }

    @Override
    public boolean isFailed() {
        return STATUS_FAILED == getHttpCode();
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "httpCode=" + httpCode +
                ", httpMsg='" + httpMsg + '\'' +
                ", errorCode=" + errorCode +
                ", throwable=" + throwable +
                '}';
    }
}
