package com.cjf.http.interceptor;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.OkHttpPlugins;
import com.cjf.http.encrypt.EncryptFunction;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: EncryptInterceptor </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:55
 */
public class EncryptInterceptor implements Interceptor {

    @NonNull
    private final EncryptFunction mEncryptFunction;

    public EncryptInterceptor(@NonNull EncryptFunction encryptFunction) {
        mEncryptFunction = encryptFunction;
    }

    @NotNull
    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        @NonNull final Request request = chain.request();
        // 是否加密
        @Nullable boolean isEncrypt = OkHttpPlugins.isInterceptorEncrypt();
        @Nullable final String headerEncrypt = request.header(EncryptFunction.HEADER_IS_ENCRYPT);
        if (!TextUtils.isEmpty(headerEncrypt)) {
            isEncrypt = Boolean.parseBoolean(headerEncrypt);
        }
        // 是否解密
        @Nullable boolean isDecoder = OkHttpPlugins.isInterceptorDecoder();
        @Nullable final String headerDecoder = request.header(EncryptFunction.HEADER_IS_DECODER);
        if (!TextUtils.isEmpty(headerDecoder)) {
            isEncrypt = Boolean.parseBoolean(headerDecoder);
        }
        //字符集
        @NonNull final String method = request.method().toLowerCase().trim();//请求方式例如：get delete put post
        @NonNull final String url = request.url().toString();

        if (!isEncrypt) {
            // 不加密
            return noEncrypt(chain, request, isDecoder, method, url);
        }
        if (!mEncryptFunction.isEncrypt(request)) {
            // 不需要加密的请求
            return noEncrypt(chain, request, isDecoder, method, url);
        }
        // 加密请求
        //如果请求方式是Get或者Delete，此时请求数据是拼接在请求地址后面的
        if (method.equals("get") || url.contains("?")) {
            // 加密请求
            @NonNull final Request encryptRequest = mEncryptFunction.encryptUrl(request);
            //响应
            @NonNull final Response response = chain.proceed(encryptRequest);
            if (response.isSuccessful()) { //只有约定的返回码才经过加密，才需要走解密的逻辑
                if (!isDecoder) {
                    // 不需要解密
                    return response;
                }
                return mEncryptFunction.decryptUrl(response, encryptRequest);
            }
            return response;
        }
        // 加密请求
        @NonNull final Request encryptRequest = mEncryptFunction.encryptBody(request);
        //响应
        @NonNull final Response response = chain.proceed(encryptRequest);
        if (response.isSuccessful()) { //只有约定的返回码才经过加密，才需要走解密的逻辑
            if (!isDecoder) {
                // 不需要解密
                return response;
            }
            return mEncryptFunction.decryptBody(response, encryptRequest);
        }
        return response;
    }

    @NotNull
    private Response noEncrypt(@NonNull Chain chain, @NonNull Request request, boolean isDecoder,
            @NonNull String method, @NonNull String url) throws IOException {
        //如果请求方式是Get或者Delete，此时请求数据是拼接在请求地址后面的
        if (method.equals("get") || url.contains("?")) {
            //响应
            @NonNull final Response response = chain.proceed(request);
            if (response.isSuccessful()) { //只有约定的返回码才经过加密，才需要走解密的逻辑
                if (!isDecoder) {
                    // 不需要解密
                    return response;
                }
                return mEncryptFunction.decryptUrl(response, request);
            }
            return response;
        }
        //响应
        @NonNull final Response response = chain.proceed(request);
        if (response.isSuccessful()) { //只有约定的返回码才经过加密，才需要走解密的逻辑
            if (!isDecoder) {
                // 不需要解密
                return response;
            }
            return mEncryptFunction.decryptBody(response, request);
        }
        return response;
    }
}
