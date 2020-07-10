package com.cjf.http.function;

import androidx.annotation.Keep;

import okhttp3.OkHttpClient;

/**
 * <p>Title: OkHttpClientBuilderFunction </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:14
 */
public interface OkHttpClientBuilderFunction {

    OkHttpClient.Builder getOkHttpClientBuilder(OkHttpClient.Builder builder);
}
