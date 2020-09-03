package com.cjf.http.converter;

import androidx.annotation.NonNull;

import com.cjf.http.utils.GsonUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * <p>Title: GsonConverter </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/22 10:05
 */
public abstract class GsonConverter implements IConverter {

    private final Gson gson;

    public static GsonConverter create() {
        return create(GsonUtil.buildGson());
    }

    public static GsonConverter create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new GsonConverter(gson) {
        };
    }

    private GsonConverter(Gson gson) {
        this.gson = gson;
    }

    @NonNull
    @Override
    public <T> T convert(@NonNull String body, @NonNull Type type) throws IOException {
        if (type == String.class) {
            return (T) body;
        }
        return gson.fromJson(body, type);
    }
}
