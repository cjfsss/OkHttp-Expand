package com.cjf.okhttp_expand.constant;

/**
 * <p>Title: SimpleConstants </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:54
 */
public interface HttpConstants {
    /**
     * 加载中
     */
    int STATUS_LOADING = 1;
    /**
     * 加载成功
     */
    int STATUS_SUCCESS = 2;
    /**
     * 加载成功,服务端报错
     */
    int STATUS_SERVICE_SUCCESS = 3;
    /**
     * 加载失败
     */
    int STATUS_FAILED = 4;
    /**
     * 数据为空
     */
    int STATUS_EMPTY_DATA = 5;
    /**
     * 没有网络
     */
    int STATUS_NO_NETWORK = 6;
    /**
     * 网络异常
     */
    int STATUS_NETWORK_EXCEPTION = 7;

    String HTTP_MSG_NO_DATA = "没有数据";

    String HTTP_MSG_SUCCESS_SUCCESS = "请求成功";

    String HTTP_MSG_SUCCESS_ERROR = "请求失败，请稍候重试！";

    String HTTP_MSG_NO_NET = "当前无网络，请检查你的网络设置！";

    String HTTP_MSG_NET_EXCEPTION = "网络连接不可用，请稍后重试！";

    String HTTP_MSG_SOCKET_TIMEOUT = "连接超时,请稍后再试！";

    String HTTP_MSG_CONNECT_EXCEPTION = "网络不给力，请稍候重试！";

    String HTTP_MSG_LOCATION_EXCEPTION = "查询异常，请联系管理员！";

}
