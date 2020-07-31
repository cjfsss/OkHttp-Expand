package com.cjf.http.encrypt;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: EncryptFunction </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/6/5 20:38
 */
public interface EncryptFunction {

    String HEADER_IS_ENCRYPT = "HEADER_IS_ENCRYPT";
    String HEADER_IS_DECODER = "HEADER_IS_DECODER";

    /**
     * 是否进行加密
     *
     * @param request 请求体
     * @return true 加密
     */

    boolean isEncrypt(@NonNull Request request);

    /**
     * 加密请求
     *
     * @param request 请求
     * @return 响应体
     */
    @NonNull
    Request encryptBody(@NonNull Request request) throws IOException;

    /**
     * 解密请求
     *
     * @param response       加密
     * @param encryptRequest 请求体
     * @return 响应体
     */
    @NonNull
    Response decryptBody(@NonNull Response response, @NonNull Request encryptRequest) throws IOException;

    /**
     * 加密请求
     *
     * @param request 请求
     * @return 响应体
     */
    @NonNull
    Request encryptUrl(@NonNull Request request) throws IOException;

    /**
     * 解密请求
     *
     * @param response       加密
     * @param encryptRequest 请求体
     * @return 响应体
     */
    @NonNull
    Response decryptUrl(@NonNull Response response, @NonNull Request encryptRequest) throws IOException;
}
