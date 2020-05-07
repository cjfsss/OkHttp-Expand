package com.cjf.okhttp_expand.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cjf.okhttp_expand.OkHttpFactory;
import com.cjf.okhttp_expand.constant.HttpConstants;
import com.cjf.okhttp_expand.constant.ResponseManager;
import com.cjf.okhttp_expand.entities.IResponseError;
import com.cjf.okhttp_expand.exception.HttpStatusCodeException;
import com.cjf.okhttp_expand.exception.NetworkError;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 异常处理帮助类
 * User: ljx
 * Date: 2019/04/29
 * Time: 11:15
 */
public class ExceptionHelper implements HttpConstants {

    //处理网络异常
    public static IResponseError handleNetworkException(Throwable throwable) {
        if (throwable instanceof NetworkError) {
            if (!isNetworkConnected(OkHttpFactory.getInstance().getApplication())) {
                return ResponseManager.createNoNet(throwable);
            } else {
                return ResponseManager.createNetException(throwable);
            }
        } else if (throwable instanceof UnknownHostException) {
            if (!isNetworkConnected(OkHttpFactory.getInstance().getApplication())) {
                return ResponseManager.createNoNet(throwable);
            } else {
                return ResponseManager.createNetException(throwable);
            }
        } else if (throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
            //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
            return ResponseManager.createFailed(HTTP_MSG_SOCKET_TIMEOUT, throwable);
        } else if (throwable instanceof ConnectException) {
            return ResponseManager.createFailed(HTTP_MSG_CONNECT_EXCEPTION, throwable);
        } else {
            return ResponseManager.createFailed(HTTP_MSG_SUCCESS_ERROR, throwable);
        }
    }

    /**
     * 根据Http执行结果过滤异常
     *
     * @param response Http响应体
     * @throws IOException 请求失败异常、网络不可用异常
     */
    public static ResponseBody throwIfFatal(Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null)
            throw new HttpStatusCodeException(response);
        if (!response.isSuccessful()) {
            throw new HttpStatusCodeException(response, body.string());
        }
        return body;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }
}
