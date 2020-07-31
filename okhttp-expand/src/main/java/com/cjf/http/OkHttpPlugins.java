package com.cjf.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.converter.GsonConverter;
import com.cjf.http.converter.IConverter;
import com.cjf.http.exception.OkHttpExceptionHelper;

import kotlin.jvm.functions.Function1;

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
    private static Function1<? super String, String> decoder;

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
    public static void setResultDecoder(@Nullable Function1<? super String, String> decoder) {
        OkHttpPlugins.decoder = decoder;
    }

    /**
     * 对字符串进行解码/解密
     *
     * @param source String字符串
     * @return 解码/解密后字符串
     */
    @NonNull
    public static String onResultDecoder(@NonNull String source) {
        Function1<? super String, String> f = decoder;
        if (f != null) {
            return apply(f, source);
        }
        return source;
    }

    @NonNull
    private static <T, R> R apply(@NonNull Function1<T, R> f, @NonNull T t) {
        try {
            return f.invoke(t);
        } catch (Throwable ex) {
            throw OkHttpExceptionHelper.wrapOrThrow(ex);
        }
    }
}
