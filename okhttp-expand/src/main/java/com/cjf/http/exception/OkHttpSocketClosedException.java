package com.cjf.http.exception;

import androidx.annotation.Keep;

import java.io.IOException;

/**
 * <p>Title: SocketClosedException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:04
 */
public class OkHttpSocketClosedException extends IOException {

    public OkHttpSocketClosedException(String message) {
        super(message);
    }

    public OkHttpSocketClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OkHttpSocketClosedException(Throwable cause) {
        super(cause);
    }
}
