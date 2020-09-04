package com.cjf.http.interceptor;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.OkHttpPlugins;
import com.cjf.http.exception.OkHttpHostException;
import com.cjf.http.exception.OkHttpNetworkError;
import com.cjf.http.exception.OkHttpSocketClosedException;
import com.cjf.http.exception.OkHttpUrlException;
import com.cjf.http.network.Network;
import com.cjf.http.utils.IOUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;

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
        if (!mNetwork.isNetAvailable()) {
            throw new OkHttpNetworkError(String.format("Network Unavailable: %1$s.", request.url()), request);
        }
        try {
            // 判断是否可以进行加密
            @Nullable boolean isEncrypt = OkHttpPlugins.isInterceptorEncrypt();
            if (isEncrypt) {
                String body = IOUtils.readRequest(request);
                String bodyEncrypt = OkHttpPlugins.onResultEncrypt(request, body);
                Request requestEncrypt = IOUtils.writeRequest(request, bodyEncrypt);
                return chain.proceed(requestEncrypt);
            }
            return chain.proceed(request);
        } catch (SocketException e) {
            if (TextUtils.equals("Socket closed", e.getMessage())) {
                throw new OkHttpSocketClosedException(String.format("Socket closed exception: %1$s.", request.url()),
                                                      request, e);
            }
            throw e;
        } catch (MalformedURLException e) {
            throw new OkHttpUrlException(String.format("url error exception: %1$s.", request.url()), request, e);
        } catch (UnknownHostException e) {
            throw new OkHttpHostException(String.format("host error exception: %1$s.", request.url()), request, e);
        }
    }
}
