package com.cjf.http.utils;


import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * User: ljx
 * Date: 2018/01/19
 * Time: 10:46
 */
public class GsonUtil {

    private static Gson gson;

    /**
     * json字符串转对象，解析失败，不会抛出异常，会直接返回null
     *
     * @param json json字符串
     * @param type 对象类类型
     * @param <T>  返回类型
     * @return T，返回对象有可能为空
     */
    @Nullable
    public static <T> T fromJsonNull(String json, Type type) {
        try {
            return fromJson(json, type);
        } catch (Throwable ignore) {
            return null;
        }
    }

    /**
     * json字符串转对象，解析失败，将抛出对应的{@link JsonSyntaxException}异常，根据异常可查找原因
     *
     * @param json json字符串
     * @param type 对象类类型
     * @param <T>  返回类型
     * @return T，返回对象不为空
     */
    @NonNull
    public static <T> T fromJson(String json, Type type) {
        Gson gson = buildGson();
        return gson.fromJson(json, type);
    }

    /**
     * json字符串转对象，解析失败，将抛出对应的{@link JsonSyntaxException}异常，根据异常可查找原因
     *
     * @param json json字符串
     * @param type 对象类类型
     * @param <T>  返回类型
     * @return T，返回对象不为空
     */
    @NonNull
    public static <T> T fromJsonArray(String json, Class<T> type) {
        Type typeToken = TypeToken.getParameterized(List.class, type).getType();
        return fromJson(json, typeToken);
    }

    public static String toJson(Object object) {
        return buildGson().toJson(object);
    }

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(String.class, new StringAdapter())
                                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                                    .registerTypeAdapter(Long.class, new LongDefault0Adapter()).create();
        }
        return gson;
    }

    private static class StringAdapter implements JsonSerializer<String>,
                                                  JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            if (json instanceof JsonPrimitive) {
                String result = json.getAsString();
                if (result == null || result.length() == 0 || TextUtils.equals(result, "null") ||
                    TextUtils.equals(result, "NULL")) {
                    return "";
                }
                return result;
            } else {
                String result = json.toString();
                //noinspection ConstantConditions
                if (result == null || result.length() == 0 || TextUtils.equals(result, "null") ||
                    TextUtils.equals(result, "NULL")) {
                    return "";
                }
                return result;
            }
        }

        @Override
        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class IntegerDefault0Adapter implements JsonSerializer<Integer>,
                                                           JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            try {
                String result = json.getAsString();
                if (result == null || result.length() == 0 || TextUtils.equals(result, "null") ||
                    TextUtils.equals(result, "NULL")) {//定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class DoubleDefault0Adapter implements JsonSerializer<Double>,
                                                          JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            try {
                String result = json.getAsString();
                if (result == null || result.length() == 0 || TextUtils.equals(result, "null") ||
                    TextUtils.equals(result, "NULL")) {//定义为double类型,如果后台返回""或者null,则返回0.00
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class LongDefault0Adapter implements JsonSerializer<Long>,
                                                        JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            try {
                String result = json.getAsString();
                if (result == null || result.length() == 0 || TextUtils.equals(result, "null") ||
                    TextUtils.equals(result, "NULL")) {//定义为long类型,如果后台返回""或者null,则返回0
                    return 0L;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsLong();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }
}