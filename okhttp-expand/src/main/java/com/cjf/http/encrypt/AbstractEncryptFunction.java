package com.cjf.http.encrypt;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.exception.OkHttpEncryptException;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * <p>Title: DefaultEncryptFunction </p>
 * <p>Description: 加密解密 </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/6/5 20:48
 */
public abstract class AbstractEncryptFunction implements EncryptFunction {

    @Override
    public boolean isEncrypt(@NonNull Request request) {
        //字符集
        @NonNull final String method = request.method().toLowerCase().trim();//请求方式例如：get delete put post
        @NonNull final String url = request.url().toString();
        //如果请求方式是Get或者Delete，此时请求数据是拼接在请求地址后面的
        if (method.equals("get") && !url.contains("?")) {
            // get 没有参数，不加密
            return false;
        }
        if (method.equals("head") && !url.contains("?")) {
            // head 没有参数，不加密
            return false;
        }
        //不是Get和Delete请求时，则请求数据在请求体中
        @Nullable final RequestBody requestBody = request.body();
        if (requestBody == null) {
            // "get" "head"
            return true;
        }
        //判断类型
        @Nullable final MediaType contentType = requestBody.contentType();
        if (contentType == null) {
            return false;
        }
        /*如果是二进制上传  则不进行加密*/
        if (TextUtils.equals("multipart", contentType.type().toLowerCase())) {
            return false;
        }
        // 所有请求默认加密
        if (isEncryptUrl(url)) {
            return true;
        }
        return true;
    }

    /**
     * 通过Url判断是否加密
     *
     * @param url 请求地址
     * @return true 加密
     */
    protected boolean isEncryptUrl(@NonNull final String url) {
        return true;
    }

    @Override
    @NonNull
    public Request encryptBody(@NonNull Request request) throws IOException {
        @Nullable final RequestBody requestBody = request.body();
        if (requestBody == null) {
            throw new OkHttpEncryptException("requestBody must be not null", request);
        }
        @NonNull final String method = request.method();//请求方式例如：get delete put post
        //字符集
        @NonNull final Charset charset;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            charset = StandardCharsets.UTF_8;
        } else {
            charset = StandardCharsets.UTF_8;
        }
        // 获取请求的数据
        @NonNull final Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        @NonNull final String requestData = URLDecoder.decode(buffer.readString(charset).trim(), "utf-8");
        buffer.close();
        //这里调用加密的方法，自行修改
        @NonNull final byte[] encryptData = encrypt(requestData);
        //构建新的请求体
        @NonNull final RequestBody newRequestBody = RequestBody.create(encryptData, requestBody.contentType());
        //构建新的requestBuilder
        @NonNull final Request.Builder newRequestBuilder = request.newBuilder();
        //根据请求方式构建相应的请求
        return newRequestBuilder.method(method, newRequestBody).build();
    }

    @Override
    @NonNull
    public Response decryptBody(@NonNull Response response, @NonNull Request encryptRequest) throws IOException {
        @Nullable final ResponseBody body = response.body();
        if (body == null) {
            return response;
        }
        @Nullable final MediaType mediaType = body.contentType();
        // 解密数据
        byte[] bytes = decrypt(body);
        return response.newBuilder().body(ResponseBody.create(bytes, mediaType)).build();
    }


    @NonNull
    @Override
    public Request encryptUrl(@NonNull Request request) throws IOException {
        @NonNull final HttpUrl url = request.url();
        //  userName=xiaoming&userPassword=12345
        @Nullable final String requestData = url
                .encodedQuery();// query 是获取到的拼接字符串 类似 userName=xiaoming&userPassword=12345
        /*如果有请求数据 则加密*/
        if (requestData != null) {
            //http://192.168.3.138:8089/interface/login   //@get @delete 时候需要拼接在请求地址后面  ?userName=xiaoming&userPassword=12345
            @NonNull final String scheme = url.scheme();//  http  https
            @NonNull final String host = url.host();//   192.168.3.138
            @NonNull final int port = url.port();//   8089
            @NonNull final String path = url.encodedPath();//  /interface/login
            @NonNull final String originalPath = scheme + "://" + host + ":" + port + path;
            // 加密数据
            @NonNull final byte[] encryptData = encrypt(requestData);
            //拼接加密后的url，参数字段自己跟后台商量，这里我用param，后台拿到数据先对param进行解密，解密后的数据就是请求的数据
            @NonNull final String newUrl = originalPath + "?" + new String(encryptData, Charset.defaultCharset());
            //构建新的请求
            return request.newBuilder().url(newUrl).build();
        }
        return request;
    }

    @NonNull
    @Override
    public Response decryptUrl(@NonNull Response response, @NonNull Request encryptRequest) throws IOException {
        @Nullable final ResponseBody body = response.body();
        if (body == null) {
            return response;
        }
        @Nullable final MediaType mediaType = body.contentType();
        // 解密数据
        @NonNull final byte[] bytes = decrypt(body);
        return response.newBuilder().body(ResponseBody.create(bytes, mediaType)).build();
    }

    /**
     * 加密
     *
     * @param requestData 需要加密的数据
     * @return 加密后的数据
     */
    @NonNull
    protected abstract byte[] encrypt(@NonNull final String requestData) throws IOException;

    /**
     * 解密
     *
     * @param body 需要解密的数据
     * @return 解密后的数据
     */
    @NonNull
    protected abstract byte[] decrypt(@NonNull final ResponseBody body) throws IOException;
}
