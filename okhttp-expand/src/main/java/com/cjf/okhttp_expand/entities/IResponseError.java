package com.cjf.okhttp_expand.entities;

/**
 * <p>Title: IResponseSuccess </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 19:00
 */
public interface IResponseError {
    /**
     * 当前无网络，请检查你的网络设置
     * @return true 当前无网络，请检查你的网络设置
     */
    boolean isNoNetWork();

    /**
     * 网络连接不可用，请稍后重试！ 网络不给力，请稍候重试！ 连接超时,请稍后再试
     * @return true
     */
    boolean isNetWork();

    boolean isFailed();

}
