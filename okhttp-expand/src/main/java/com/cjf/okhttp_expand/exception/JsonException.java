package com.cjf.okhttp_expand.exception;

/**
 * <p>Title: JsonSyntaxException </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/29 16:08
 */
public class JsonException extends RuntimeException {

    public JsonException(String msg) {
        super(msg);
    }

    public JsonException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates exception with the specified cause. Consider using
     * {@link #JsonException(String, Throwable)} instead if you can
     * describe what actually happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    public JsonException(Throwable cause) {
        super(cause);
    }
}
