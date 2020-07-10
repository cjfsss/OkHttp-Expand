package com.cjf.http.exception;

import androidx.annotation.Keep;

/**
 * <p>Title: OkHttpNullException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/6/5 22:18
 */
public class OkHttpNullException extends NullPointerException {

    public OkHttpNullException() {
    }

    public OkHttpNullException(String s) {
        super(s);
    }
}
