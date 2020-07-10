package com.cjf.http.network;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * <p>Title: Network </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 16:21
 */
public interface Network {

    /**
     * Check the network is enable.
     */
    boolean isNetAvailable();

    /**
     * 添加网络监听
     */
    void addNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener);

    /**
     * 移除网络监听
     */
    void removeNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener);

    /**
     * 移除所有监听
     */
    void removeAll();


    void onDestroy();
}
