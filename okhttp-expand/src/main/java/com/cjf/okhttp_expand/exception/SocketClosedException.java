package com.cjf.okhttp_expand.exception;

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
public class SocketClosedException extends IOException {

    public SocketClosedException(String message) {
        super(message);
    }

    public SocketClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketClosedException(Throwable cause) {
        super(cause);
    }
}
