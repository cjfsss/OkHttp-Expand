package com.cjf.http.exception;

import androidx.annotation.Keep;

/**
 * <p>Title: JsonSyntaxException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/29 16:08
 */
public class OkHttpJsonException extends RuntimeException {

    public OkHttpJsonException(String msg) {
        super(msg);
    }

    public OkHttpJsonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #OkHttpJsonException(String, Throwable)} instead if you can
     * describe what actually happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public OkHttpJsonException(Throwable cause) {
        super(cause);
    }
}
