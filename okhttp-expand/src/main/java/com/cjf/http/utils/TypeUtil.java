package com.cjf.http.utils;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: ljx
 * Date: 2019-06-16
 * Time: 13:36
 */
public class TypeUtil {

    /**
     * 获取泛型类型
     *
     * @param clazz 类类型
     * @param index 第几个泛型
     * @return Type
     */
    public static <T> Type getActualTypeParameter(Class<T> clazz, int index) {
        Type superclass = clazz.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[index]);
    }

    /**
     * Gets type literal for the given {@code Type} instance.
     */
    public static TypeToken<?> get(Type type) {
        return TypeToken.get(type);
    }

    /**
     * Gets type literal for the given {@code Class} instance.
     */
    public static <T> TypeToken<T> get(Class<T> type) {
        return TypeToken.get(type);
    }

    /**
     * Gets type literal for the parameterized type represented by applying {@code typeArguments} to
     * {@code rawType}.
     */
    public static TypeToken<?> getParameterized(Type rawType, Type... typeArguments) {
        return TypeToken.getParameterized(rawType, typeArguments);
    }

    /**
     * Gets type literal for the array type whose elements are all instances of {@code componentType}.
     */
    public static TypeToken<?> getArray(Type componentType) {
        return TypeToken.getArray(componentType);
    }

}
