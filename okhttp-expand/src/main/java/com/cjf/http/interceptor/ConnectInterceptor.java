package com.cjf.http.interceptor;


import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.cjf.http.exception.OkHttpNetworkError;
import com.cjf.http.exception.OkHttpSocketClosedException;
import com.cjf.http.network.Network;

import org.jetbrains.annotations.NotNull;

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

    public ConnectInterceptor(@NonNull final Network network) {
        mNetwork = network;
    }

    @NotNull
    @Override
    public okhttp3.Response intercept(@NonNull final Chain chain) throws IOException {
        final Request request = chain.request();
        if (!mNetwork.isNetAvailable())
            throw new OkHttpNetworkError(String.format("Network Unavailable: %1$s.", request.url()));
        try {
            return chain.proceed(request);
        } catch (SocketException e) {
            if (TextUtils.equals("Socket closed", e.getMessage())) {
                throw new OkHttpSocketClosedException(String.format("Socket closed exception: %1$s.", request.url()), e);
            }
            throw e;
        }
    }

}
