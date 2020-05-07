package com.cjf.okhttp_expand.entities;

import android.text.TextUtils;

import java.util.List;

/**
 * <p>Title: IData </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 18:55
 */
public interface IData {

    <T> T get(Class<T> clazz);

    @SuppressWarnings("unchecked")
    default <T> List<T> getList(Class<T> clazz) {
        return get(List.class);
    }

    default String getString() {
        String value = get(String.class);
        return TextUtils.isEmpty(value)
                || TextUtils.equals(value, "null")
                || TextUtils.equals(value, "NULL")
                ? "" : value;
    }

    default boolean getBoolean() {
        Boolean value = get(Boolean.class);
        return value == null ? false : value;
    }

    default long getLong() {
        Long value = get(Long.class);
        return value == null ? 0 : value;
    }

    default int getInteger() {
        Integer value = get(Integer.class);
        return value == null ? 0 : value;
    }

    default float getFloat() {
        Float value = get(Float.class);
        return value == null ? 0 : value;
    }

    default double getDouble() {
        Double value = get(Double.class);
        return value == null ? 0 : value;
    }

    default short getShort() {
        Short value = get(Short.class);
        return value == null ? 0 : value;
    }
}
