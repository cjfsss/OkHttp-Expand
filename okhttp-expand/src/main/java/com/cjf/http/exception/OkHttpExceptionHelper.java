package com.cjf.http.exception;

import androidx.annotation.NonNull;

import java.io.IOException;

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
            throw new OkHttpStatusCodeException("body == null", response);
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
}
