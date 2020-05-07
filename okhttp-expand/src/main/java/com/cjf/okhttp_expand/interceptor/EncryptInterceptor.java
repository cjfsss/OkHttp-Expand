package com.cjf.okhttp_expand.interceptor;


import com.cjf.okhttp_expand.convert.EncryptConvert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    /**
     * 不需要加密的请求,优先级高
     */
    private List<String> mUrlNoEncryptionList;
    /**
     * 需要加密的请求
     */
    private List<String> mUrlEncryptionList;

    private EncryptConvert mEncryptConvert;

    public EncryptInterceptor noEncryptionList(String... urlNoEncryptionList) {
        mUrlNoEncryptionList = Arrays.asList(urlNoEncryptionList);
        return this;
    }

    public EncryptInterceptor encryptionList(String... urlEncryptionList) {
        mUrlEncryptionList = Arrays.asList(urlEncryptionList);
        return this;
    }

    public EncryptInterceptor encryptConvert(final EncryptConvert encryptConvert) {
        mEncryptConvert = encryptConvert;
        return this;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();
        final String url = request.url().toString();
        if (mUrlNoEncryptionList != null && mUrlNoEncryptionList.contains(url)) {
            // 不需要加密的请求
            return chain.proceed(request);
        }
        if (mUrlEncryptionList != null && !mUrlEncryptionList.contains(url)) {
            // 不需要加密的请求
            return chain.proceed(request);
        }
        if (mEncryptConvert == null) {
            throw new NullPointerException("please implements EncryptConvert");
        }
        // 加密请求
        final Request encryptRequest = mEncryptConvert.encrypt(request);
        //响应
        final Response response = chain.proceed(encryptRequest);
        if (response.code() == 200) { //只有约定的返回码才经过加密，才需要走解密的逻辑
            return mEncryptConvert.decrypt(response);
        }
        return response;
    }
}
