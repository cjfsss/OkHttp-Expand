package com.cjf.http.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.okhttp_expand.R;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <p>Title: OkHttpExceptionHelper </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/20 15:03
 */
public class OkHttpExceptionHelper {

    /**
     * 根据Http执行结果过滤异常
     *
     * @param response Http响应体
     * @return ResponseBody
     * @throws IOException 请求失败异常、网络不可用异常
     */
    @NonNull
    public static ResponseBody throwIfFatal(@NonNull Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            throw new OkHttpResponseBodyNullException("ResponseBody must be not null", response);
        }
        if (!response.isSuccessful()) {
            throw new OkHttpStatusCodeException("response normal error", response, body.string());
        }
        return body;
    }

    /**
     * If the provided Throwable is an Error this method
     * throws it, otherwise returns a RuntimeException wrapping the error
     * if that error is a checked exception.
     *
     * @param error the error to wrap or throw
     * @return the (wrapped) error
     */
    public static RuntimeException wrapOrThrow(Throwable error) {
        if (error instanceof Error) {
            throw (Error) error;
        }
        if (error instanceof RuntimeException) {
            return (RuntimeException) error;
        }
        return new RuntimeException(error);
    }

    @Nullable
    public static String throwToMessage(@NonNull Throwable throwable) {
        if (throwable instanceof OkHttpNetworkError) {
            // Network Unavailable
            return "请求失败，请检查网络设置！";
        } else if (throwable instanceof OkHttpSuccessExecuteException) {
            // Socket closed exception
            return "请求结果异常，请稍后再试！";
        } else if (throwable instanceof OkHttpSocketClosedException) {
            // Socket closed exception
            return "请求被中断，请稍后再试！";
        } else if (throwable instanceof SocketTimeoutException) {
            // Socket timeout exception
            return "服务器请求超时，请稍后再试！";
        } else if (throwable instanceof OkHttpConnectException) {
            // Socket code exception
            return "服务器连接异常，请稍后再试！";
        } else if (throwable instanceof OkHttpResponseBodyNullException) {
            // Socket code exception
            return "服务器连接异常，请稍后再试！";
        } else if (throwable instanceof OkHttpRequestBodyNullException) {
            // Socket code exception
            return "服务器连接异常，请稍后再试！";
        } else if (throwable instanceof OkHttpStatusCodeException) {
            // Socket code exception
            return "服务器连接异常，请稍后再试！";
        } else if (throwable instanceof OkHttpUrlException) {
            // Socket url host exception
            return "访问地址地址错误，请稍后再试！";
        } else if (throwable instanceof OkHttpHostException) {
            // Socket url host exception
            return "访问地址地址错误，请稍后再试！";
        } else if (throwable instanceof OkHttpEncryptException) {
            // Socket timeout exception
            return "数据解析异常，请稍后再试!";
        } else if (throwable instanceof OkHttpDecryptException) {
            // Socket timeout exception
            return "数据解析异常，请稍后再试!";
        } else if (throwable instanceof OkHttpJsonException) {
            // Socket timeout exception
            return "数据解析异常，请稍后再试!";
        } else {
            return throwable.getMessage();
        }
    }
}
