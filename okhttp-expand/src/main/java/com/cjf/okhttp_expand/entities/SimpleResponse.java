package com.cjf.okhttp_expand.entities;

import com.cjf.okhttp_expand.constant.HttpConstants;

import java.io.Serializable;

/**
 * <p>Title: SimpleResponse </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 19:13
 */
public class SimpleResponse implements IResponseSuccess, HttpConstants, Serializable {

    private static final long serialVersionUID = 3040492078800879758L;

    /**
     * 服务端返回的状态码
     */
    private int state;
    /**
     * 服务端返回的提示信息
     */
    private String message;

    private Object data;

    public SimpleResponse() {
    }


    public SimpleResponse(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public SimpleResponse(int state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public <T> T get(Class<T> clazz) {
        return clazz.cast(data);
    }

    @Override
    public String toString() {
        return "SimpleResponse{" +
                "state=" + state +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
