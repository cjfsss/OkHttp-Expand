package com.cjf.okhttp_expand.interceptor;


import android.text.TextUtils;

import com.cjf.okhttp_expand.exception.NetworkError;
import com.cjf.okhttp_expand.exception.SocketClosedException;
import com.cjf.okhttp_expand.network.Network;

import java.io.IOException;
import java.net.SocketException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * <p>Title: ConnectInterceptor </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:54
 */
public class ConnectInterceptor implements Interceptor {

    private final Network mNetwork;

    public ConnectInterceptor(final Network network) {
        mNetwork = network;
    }

    @Override
    public okhttp3.Response intercept(final Interceptor.Chain chain) throws IOException {
        final Request request = chain.request();
        if (!mNetwork.isNetAvailable())
            throw new NetworkError(String.format("Network Unavailable: %1$s.", request.url()));
        try {
            return chain.proceed(request);
        } catch (SocketException e) {
            if (TextUtils.equals("Socket closed", e.getMessage())) {
                throw new SocketClosedException(String.format("Socket closed exception: %1$s.", request.url()), e);
            }
            throw e;
        }
    }

}
