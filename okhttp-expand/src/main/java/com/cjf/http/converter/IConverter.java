package com.cjf.http.converter;

import androidx.annotation.NonNull;

import com.cjf.http.OkHttpPlugins;
import com.cjf.http.exception.OkHttpExceptionHelper;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <p>Title: IConverter </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/22 10:01
 */
public interface IConverter {

    /**
     * 请求结束后拿到 String 转 对象
     *
     * @param source String
     * @param type 对象类型
     * @param <T>  T
     * @return T
     * @throws IOException 转换失败异常
     */
    @NonNull
    <T> T convert(@NonNull String source, @NonNull Type type) throws IOException;

    /**
     * 请求结束后拿到 ResponseBody 转 对象
     *
     * @param source            String
     * @param type            对象类型
     * @param onResultDecoder 是否需要对结果进行解码/解密，可根据此字段判断,
     * @param <T>             T
     * @return T
     * @throws IOException 转换失败异常
     */
    @NonNull
    default <T> T convert(@NonNull String source, @NonNull Type type, boolean onResultDecoder) throws IOException {
        if (onResultDecoder) {
            source = decoder(source);
        }
        return convert(source, type);
    }

    /**
     * 请求结束后拿到 ResponseBody 转 对象
     *
     * @param body            ResponseBody
     * @param type            对象类型
     * @param onResultDecoder 是否需要对结果进行解码/解密，可根据此字段判断,
     * @param <T>             T
     * @return T
     * @throws IOException 转换失败异常
     */
    @NonNull
    default <T> T convert(@NonNull ResponseBody body, @NonNull Type type, boolean onResultDecoder) throws IOException {
        String source = body.string();
        if (onResultDecoder) {
            source = decoder(source);
        }
        return convert(source, type);
    }
    /**
     * 请求结束后拿到 Response 转 对象
     *
     * @param response            Response
     * @param type            对象类型
     * @param onResultDecoder 是否需要对结果进行解码/解密，可根据此字段判断,
     * @param <T>             T
     * @return T
     * @throws IOException 转换失败异常
     */
    @NonNull
    default <T> T convert(@NonNull Response response, @NonNull Type type, boolean onResultDecoder) throws IOException {
        String source = OkHttpExceptionHelper.throwIfFatal(response).string();
        if (onResultDecoder) {
            source = decoder(source);
        }
        return convert(source, type);
    }

    @NonNull
    default String decoder(@NonNull String source) {
        return OkHttpPlugins.onResultDecoder(source);
    }
}
