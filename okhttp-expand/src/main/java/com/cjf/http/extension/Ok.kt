package com.cjf.http.extension

import android.os.Build
import com.cjf.http.OkHttpFactory
import com.cjf.http.OkHttpPlugins
import com.cjf.http.converter.IConverter
import com.cjf.http.encrypt.EncryptFunction
import com.cjf.http.exception.OkHttpExceptionHelper
import com.cjf.http.exception.OkHttpNullException
import com.cjf.http.network.OnNetWorkChangedListener
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException
import java.lang.reflect.Type
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * <p>Title: Ok </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @date : 2020/7/21 20:05
 * @version : 1.0
 */

fun RequestBody.string(): String? {
    //字符集
    val charset: Charset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        StandardCharsets.UTF_8
    } else {
        Charset.forName("UTF-8")
    }
    val buffer = Buffer()
    var requestData: String? = null
    buffer.use {
        writeTo(buffer)
        requestData = URLDecoder.decode(buffer.readString(charset).trim(), "utf-8")
    }
    return requestData
}

fun RequestBody.toRequestBody(bodyString: String?): RequestBody? {
    return bodyString?.toRequestBody(contentType())
}

fun Request.Builder.encryptForInterceptor(isEncrypt: Boolean = OkHttpPlugins.isInterceptorEncrypt()): Request.Builder {
    return addHeader(EncryptFunction.HEADER_IS_ENCRYPT, isEncrypt.toString())
}

fun Request.Builder.decoderForInterceptor(isDecoder: Boolean = OkHttpPlugins.isInterceptorDecoder()): Request.Builder {
    return addHeader(EncryptFunction.HEADER_IS_DECODER, isDecoder.toString())
}

@Throws(IOException::class)
fun Response.string(): String {
    return OkHttpExceptionHelper.throwIfFatal(this).string()
}

fun convert(): IConverter {
    return OkHttpPlugins.getIConverter()
}

@Throws(IOException::class)
fun <T> ResponseBody.convert(type: Class<T>, onResultDecoder: Boolean = OkHttpPlugins.isInterceptorDecoder()): T {
    use {
        return convert().convert<T>(it, type, onResultDecoder)
    }
}

@Throws(IOException::class)
fun <T> ResponseBody.convert(type: Type, onResultDecoder: Boolean = OkHttpPlugins.isInterceptorDecoder()): T {
    use {
        return convert().convert<T>(it, type, onResultDecoder)
    }
}

@Throws(IOException::class)
fun <T> Response.convert(type: Class<T>, onResultDecoder: Boolean = OkHttpPlugins.isInterceptorDecoder()): T {
    use {
        return convert().convert<T>(it, type, onResultDecoder)
    }
}

@Throws(IOException::class)
fun <T> Response.convert(type: Type, onResultDecoder: Boolean = OkHttpPlugins.isInterceptorDecoder()): T {
    use {
        return convert().convert<T>(it, type, onResultDecoder)
    }
}

fun factory(): OkHttpFactory {
    return OkHttpFactory.getInstance()
}

@Throws(OkHttpNullException::class)
fun client(): OkHttpClient {
    return factory().okHttpClient
}

fun addNetWorkChangedListener(listener: OnNetWorkChangedListener) {
    factory().addNetWorkChangedListener(listener)
}

fun removeNetWorkChangedListener(listener: OnNetWorkChangedListener) {
    factory().removeNetWorkChangedListener(listener)
}

fun isNetAvailable(): Boolean {
    return factory().isNetAvailable
}

fun cancelAll() {
    factory().cancelAll()
}

fun cancelTag(tag: Any?) {
    factory().cancelTag(tag)
}