package com.cjf.okhttp_expand.convert;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: EncryptConvert </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:54
 */
public interface EncryptConvert {
    /**
     * 加密请求
     *
     * @param request 请求
     * @return
     */
    Request encrypt(Request request) throws IOException;

    /**
     * 解密请求
     *
     * @param response 加密
     * @return
     */
    Response decrypt(Response response) throws IOException;
}
