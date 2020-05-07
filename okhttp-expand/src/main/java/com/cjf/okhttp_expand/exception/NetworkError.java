package com.cjf.okhttp_expand.exception;

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
public class NetworkError extends IOException {

    public NetworkError(String message) {
        super(message);
    }

    public NetworkError(String message, Throwable cause) {
        super(message, cause);
    }
}
