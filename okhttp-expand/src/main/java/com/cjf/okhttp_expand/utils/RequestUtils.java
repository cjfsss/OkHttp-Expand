package com.cjf.okhttp_expand.utils;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * <p>Title: RequestUtils </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 23:28
 */
public class RequestUtils {

    public static String getEncodedUrlAndParams(Request request) {
        String result;
        try {
            result = getRequestParams(request);
        } catch (IOException e) {
            e.printStackTrace();
            result = request.url().toString();
        }
        try {
            return URLDecoder.decode(result);
        } catch (Exception e) {
            return result;
        }
    }

    private static String getRequestParams(Request request) throws IOException {
        RequestBody body = request.body();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();

        if (body instanceof MultipartBody) {
            MultipartBody multipartBody = (MultipartBody) body;
            List<MultipartBody.Part> parts = multipartBody.parts();
            StringBuilder fileBuilder = new StringBuilder();
            for (int i = 0, size = parts.size(); i < size; i++) {
                MultipartBody.Part part = parts.get(i);
                RequestBody requestBody = part.body();
                Headers headers = part.headers();
                if (headers == null || headers.size() == 0) continue;
                String[] split = headers.value(0).split(";");
                String name = null, fileName = null;
                for (String s : split) {
                    if (s.equals("form-data")) continue;
                    String[] keyValue = s.split("=");
                    if (keyValue.length < 2) continue;
                    String value = keyValue[1].substring(1, keyValue[1].length() - 1);
                    if (name == null) {
                        name = value;
                    } else {
                        fileName = value;
                        break;
                    }
                }
                if (name == null) continue;
                if (requestBody.contentLength() < 1024) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String value = buffer.readUtf8();
                    urlBuilder.addQueryParameter(name, value);
                } else {
                    if (fileBuilder.length() > 0) {
                        fileBuilder.append("&");
                    }
                    fileBuilder.append(name).append("=").append(fileName);
                }
            }
            return urlBuilder.toString() + "\n\nfiles = " + fileBuilder.toString();
        }

        if (body != null) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            if (!isPlaintext(buffer)) {
                return urlBuilder.toString() + "\n\n(binary "
                        + body.contentLength() + "-byte body omitted)";
            } else {
                return urlBuilder.toString() + "\n\n" + buffer.readUtf8();
            }
        }
        return urlBuilder.toString();
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
