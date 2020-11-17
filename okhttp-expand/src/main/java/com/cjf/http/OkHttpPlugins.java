package com.cjf.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.converter.GsonConverter;
import com.cjf.http.converter.IConverter;
import com.cjf.http.exception.OkHttpExceptionHelper;

import java.io.IOException;

import kotlin.jvm.functions.Function2;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpPlugins </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/23 13:29
 */
public final class OkHttpPlugins {

    // 拦截器解密
    private static boolean isInterceptorDecoder = false;
    // 拦截器加密
    private static boolean isInterceptorEncrypt = false;
    // 返回值转换
    private static IConverter iConverter;
    // 解密方法
    private static Function2<? super Response, ? super String, String> decoder;
    // 加密方法
    private static Function2<? super Request, ? super String, String> encrypt;

    @NonNull
    public static IConverter getIConverter() {
        if (iConverter == null) {
            iConverter = GsonConverter.create();
        }
        return iConverter;
    }

    public static void setIConverter(@Nullable IConverter IConverter) {
        iConverter = IConverter;
    }

    /**
     * 设置是否解密
     *
     * @param open 是否加密解密
     */
    public static void openEncryptDecoderForInterceptor(boolean open) {
        isInterceptorEncrypt = open;
        isInterceptorDecoder = open;
    }

    /**
     * 设置是否解密
     *
     * @param encrypt 是否加密
     * @param decoder 是否解密
     */
    public static void openEncryptDecoderForInterceptor(boolean encrypt, boolean decoder) {
        isInterceptorEncrypt = encrypt;
        isInterceptorDecoder = decoder;
    }

    public static boolean isInterceptorDecoder() {
        return isInterceptorDecoder;
    }

    public static boolean isInterceptorEncrypt() {
        return isInterceptorEncrypt;
    }

    //设置解码/解密器,可用于对Http返回的String 字符串解码/解密
    public static void setResultDecoder(@Nullable Function2<? super Response, ? super String, String> decoder) {
        OkHttpPlugins.decoder = decoder;
    }

    //设置加密/加密器,可用于对Http请求加密
    public static void setResultEncrypt(@Nullable Function2<? super Request, ? super String, String> encrypt) {
        OkHttpPlugins.encrypt = encrypt;
    }

    /**
     *  对字符串进行解码/解密
     *
     * @param source String字符串
     * @return 解码/解密后字符串
     */
    @NonNull
    public static String onResultDecoder(@NonNull Response response, @NonNull String source) {
        Function2<? super Response, ? super String, String> f = decoder;
        if (f != null) {
            return apply(f, response, source);
        }
        return source;
    }

    /**
     * 对字符串进行加密/加密
     *
     * @param request 请求体
     * @return 加密后的请求体
     */
    @NonNull
    public static String onResultEncrypt(@NonNull Request request, @NonNull String source) throws IOException {
        Function2<? super Request, ? super String, String> f = encrypt;
        if (f != null) {
            return apply(f, request, source);
        }
        return source;
    }

    @NonNull
    private static <T1, T2, R> R apply(@NonNull Function2<T1, T2, R> f, @NonNull T1 t1, @NonNull T2 t2) {
        try {
            return f.invoke(t1, t2);
        } catch (Throwable ex) {
            throw OkHttpExceptionHelper.wrapOrThrow(ex);
        }
    }
}
