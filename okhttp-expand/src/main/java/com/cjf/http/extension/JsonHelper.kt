package com.cjf.http.extension

import com.cjf.http.utils.GsonUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * <p>Title: JsonHelper </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @date : 2020/9/4 23:56
 * @version : 1.0
 */

/** json相关 **/
fun Any.toJson(dateFormat: String = "yyyy-MM-dd HH:mm:ss") = GsonUtil.buildGson(dateFormat).create().toJson(this)

inline fun <reified T> String.toBean(dateFormat: String = "yyyy-MM-dd HH:mm:ss") = GsonBuilder().setDateFormat(dateFormat).create()
        .fromJson<T>(this, object : TypeToken<T>() {}.type)

