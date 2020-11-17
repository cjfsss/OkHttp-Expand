package com.cjf.http.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: ParamUtils </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/9/24 11:07
 */
public class ParamUtils {

    @NonNull
    public static Map<String, String> convertParams(@Nullable Map<String, Object> params) {
        if (params == null) {
            return new HashMap<>();
        }
        Map<String, String> requestParams = new LinkedHashMap<>();
        Set<String> set = params.keySet();
        for (String key : set) {
            Object value = params.get(key);
            requestParams.put(key, String.valueOf(value));
        }
        return requestParams;
    }
}
