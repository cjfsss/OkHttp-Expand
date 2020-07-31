package com.cjf.http.interceptor;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
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
public abstract class HeaderInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull final Chain chain) throws IOException {
        @NonNull final Request request = chain.request();
        @NonNull final Headers headers = header(request.headers().newBuilder());
        return chain.proceed(request.newBuilder().headers(headers).build());
    }

    public abstract Headers header(Headers.Builder builder);
}
