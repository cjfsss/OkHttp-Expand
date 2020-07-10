package com.cjf.http.interceptor;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.cjf.http.CommonHttp;
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
        @Nullable final String isEncrypt = request.header(CommonHttp.HEADER_IS_ENCRYPT);
        if (TextUtils.equals(isEncrypt, CommonHttp.HEADER_ENCRYPT_TRUE)) {
            // 不加密
            return chain.proceed(request);
        }
        if (!mEncryptFunction.isEncrypt(request)) {
            // 不需要加密的请求
            return chain.proceed(request);
        }
        //字符集
        @NonNull final String method = request.method().toLowerCase().trim();//请求方式例如：get delete put post
        @NonNull final String url = request.url().toString();
        //如果请求方式是Get或者Delete，此时请求数据是拼接在请求地址后面的
        if (method.equals("get") || url.contains("?")) {
            // 加密请求
            @NonNull final Request encryptRequest = mEncryptFunction.encryptUrl(request);
            //响应
            @NonNull final Response response = chain.proceed(encryptRequest);
            if (response.code() == 200) { //只有约定的返回码才经过加密，才需要走解密的逻辑
                return mEncryptFunction.decryptUrl(response, encryptRequest);
            }
            return response;
        }
        // 加密请求
        @NonNull final Request encryptRequest = mEncryptFunction.encryptBody(request);
        //响应
        @NonNull final Response response = chain.proceed(encryptRequest);
        if (response.code() == 200) { //只有约定的返回码才经过加密，才需要走解密的逻辑
            return mEncryptFunction.decryptBody(response, encryptRequest);
        }
        return response;
    }
}
