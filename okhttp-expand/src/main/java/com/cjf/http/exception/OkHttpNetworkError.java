package com.cjf.http.exception;

import androidx.annotation.Keep;

import java.io.IOException;

/**
 * <p>Title: NetworkError </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:04
 */
public class OkHttpNetworkError extends IOException {

    public OkHttpNetworkError(String message) {
        super(message);
    }

    public OkHttpNetworkError(String message, Throwable cause) {
        super(message, cause);
    }
}
