package com.cjf.http.exception;

import androidx.annotation.Keep;

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

    public OkHttpException(String message) {
        super(message);
    }

    public OkHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public OkHttpException(Throwable cause) {
        super(cause);
    }
}
