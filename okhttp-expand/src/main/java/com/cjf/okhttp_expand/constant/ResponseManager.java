package com.cjf.okhttp_expand.constant;

import com.cjf.okhttp_expand.entities.IResponseSuccess;
import com.cjf.okhttp_expand.entities.ResponseError;
import com.cjf.okhttp_expand.entities.IResponseError;
import com.cjf.okhttp_expand.entities.SimpleResponse;

/**
 * <p>Title: ResponseManager </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 22:58
 */
public final class ResponseManager implements HttpConstants {

    public static IResponseSuccess createSuccess(final Object obj) {
        return new SimpleResponse(STATUS_SUCCESS, HTTP_MSG_SUCCESS_SUCCESS, obj);
    }

    public static IResponseSuccess createSuccess(final int state, final String message) {
        return new SimpleResponse(state, message);
    }

    public static IResponseSuccess createSuccess(final int state, final String message, final Object obj) {
        return new SimpleResponse(state, message, obj);
    }

    public static IResponseError createNoNet(Throwable throwable) {
        return new ResponseError(STATUS_NO_NETWORK, HTTP_MSG_NO_NET, throwable);
    }

    public static IResponseError createNetException(Throwable throwable) {
        return new ResponseError(STATUS_NETWORK_EXCEPTION, HTTP_MSG_NET_EXCEPTION, throwable);
    }

    public static IResponseError createFailed(final String message, final Throwable throwable) {
        return new ResponseError(STATUS_FAILED, message, throwable);
    }

    public static IResponseError createFailedService(int errorCode, final String message, final Throwable throwable) {
        return new ResponseError(STATUS_SERVICE_SUCCESS, message, errorCode, throwable);
    }
}
