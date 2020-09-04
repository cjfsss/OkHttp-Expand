package com.cjf.http.utils;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.exception.OkHttpDecryptException;
import com.cjf.http.exception.OkHttpEncryptException;
import com.cjf.http.exception.OkHttpRequestBodyNullException;
import com.cjf.http.exception.OkHttpResponseBodyNullException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * <p>Title: IOUtils </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 17:33
 */
public class IOUtils {

    public static void closeQuietly(Closeable... closeable) {
        if (closeable == null) {
            return;
        }
        for (Closeable close : closeable) {
            try {
                close.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void flushQuietly(Flushable... flushable) {
        if (flushable == null) {
            return;
        }
        for (Flushable flush : flushable) {
            try {
                flush.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将请求体转换为字符串
     *
     * @param request 请求体
     */
    @SuppressWarnings("CharsetObjectCanBeUsed")
    public static String readRequest(@NonNull Request request) throws IOException {
        @Nullable final RequestBody requestBody = request.body();
        if (requestBody == null) {
            throw new OkHttpRequestBodyNullException("RequestBody must be not null", request);
        }
        @Nullable Buffer buffer = null;
        try {
            //字符集
            @NonNull final Charset charset;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                charset = StandardCharsets.UTF_8;
            } else {
                charset = Charset.forName("UTF-8");
            }
            // 获取请求的数据
            buffer = new Buffer();
            requestBody.writeTo(buffer);
            return URLDecoder.decode(buffer.readString(charset).trim(), "utf-8");
        } catch (IOException e) {
            throw new OkHttpEncryptException("Encrypt readRequest error", request, e);
        } finally {
            closeQuietly(buffer);
        }
    }

    /**
     * 将字符串写入请求体
     *
     * @param request 请求体
     * @param data    数据
     */
    @SuppressWarnings("CharsetObjectCanBeUsed")
    public static Request writeRequest(@NonNull Request request, @Nullable String data) throws IOException {
        if (data == null) {
            return request;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return writeRequest(request, data.getBytes(StandardCharsets.UTF_8));
        } else {
            return writeRequest(request, data.getBytes(Charset.forName("UTF-8")));
        }
    }

    /**
     * 将相应体转换为byte数组
     *
     * @param response 相应体
     */
    public static byte[] readResponse(@NonNull Response response) throws IOException {
        @Nullable final ResponseBody body = response.body();
        if (body == null) {
            throw new OkHttpResponseBodyNullException("ResponseBody must be not null", response);
        }
        try {
            return toByteArray(body.byteStream());
        } catch (IOException e) {
            throw new OkHttpDecryptException("Decrypt readResponse error", response, e);
        }
    }

    /**
     * 将相应体转换为byte数组
     *
     * @param data 相应体
     */
    public static Response writeResponse(@NonNull Response response, @NonNull byte[] data) throws IOException {
        @Nullable final ResponseBody body = response.body();
        if (body == null) {
            return response;
        }
        try {
            @Nullable final MediaType mediaType = body.contentType();
            return response.newBuilder().body(ResponseBody.create(data, mediaType)).build();
        } catch (Exception e) {
            throw new OkHttpDecryptException("Decrypt writeResponse error", response, e);
        }
    }

    /**
     * 将字符串写入请求体
     *
     * @param request 请求体
     * @param data    数据
     */
    public static Request writeRequest(@NonNull Request request, @Nullable byte[] data) throws IOException {
        if (data == null) {
            return request;
        }
        @Nullable final RequestBody requestBody = request.body();
        if (requestBody == null) {
            throw new OkHttpEncryptException("requestBody must be not null", request);
        }
        try {
            @NonNull final String method = request.method();//请求方式例如：get delete put post
            //构建新的请求体
            @NonNull final RequestBody newRequestBody = RequestBody.create(data, requestBody.contentType());
            //构建新的requestBuilder
            @NonNull final Request.Builder newRequestBuilder = request.newBuilder();
            //根据请求方式构建相应的请求
            return newRequestBuilder.method(method, newRequestBody).build();
        } catch (Exception e) {
            throw new OkHttpEncryptException("Encrypt writeRequest error", request, e);
        }
    }

    public static InputStream toInputStream(CharSequence input) {
        return new ByteArrayInputStream(input.toString().getBytes());
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = input.toString().getBytes(encoding);
        return new ByteArrayInputStream(bytes);
    }

    public static BufferedInputStream toBufferedInputStream(InputStream inputStream) {
        return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream :
                new BufferedInputStream(inputStream);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream outputStream) {
        return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream) outputStream :
                new BufferedOutputStream(outputStream);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedWriter toBufferedWriter(Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static String toString(InputStream input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(Reader input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(Reader input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(byte[] byteArray) {
        return new String(byteArray);
    }

    public static String toString(byte[] byteArray, String encoding) {
        try {
            return new String(byteArray, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(byteArray);
        }
    }

    public static byte[] toByteArray(Object input) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(baos);
        }
        return null;
    }

    public static Object toObject(byte[] input) {
        if (input == null) {
            return null;
        }
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(input);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(bais);
        }
        return null;
    }

    public static byte[] toByteArray(CharSequence input) {
        if (input == null) {
            return new byte[0];
        }
        return input.toString().getBytes();
    }

    public static byte[] toByteArray(CharSequence input, String encoding) throws UnsupportedEncodingException {
        if (input == null) {
            return new byte[0];
        } else {
            return input.toString().getBytes(encoding);
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output, encoding);
        output.close();
        return output.toByteArray();
    }

    public static char[] toCharArray(CharSequence input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input, String encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write(input, output);
        return output.toCharArray();
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        Reader reader = new InputStreamReader(input, encoding);
        return readLines(reader);
    }

    public static List<String> readLines(InputStream input) throws IOException {
        Reader reader = new InputStreamReader(input);
        return readLines(reader);
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(byte[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(new String(data));
        }
    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        if (data != null) {
            output.write(new String(data, encoding));
        }
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(char[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes());
        }
    }

    public static void write(char[] data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(new String(data).getBytes(encoding));
        }
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    public static void write(CharSequence data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes());
        }
    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes(encoding));
        }
    }

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1)
            outputStream.write(buffer, 0, len);
    }

    public static void write(Reader input, OutputStream output) throws IOException {
        Writer out = new OutputStreamWriter(output);
        write(input, out);
        out.flush();
    }

    public static void write(InputStream input, Writer output) throws IOException {
        Reader in = new InputStreamReader(input);
        write(in, output);
    }

    public static void write(Reader input, OutputStream output, String encoding) throws IOException {
        Writer out = new OutputStreamWriter(output, encoding);
        write(input, out);
        out.flush();
    }

    public static void write(InputStream input, OutputStream output, String encoding) throws IOException {
        Reader in = new InputStreamReader(input, encoding);
        write(in, output);
    }

    public static void write(InputStream input, Writer output, String encoding) throws IOException {
        Reader in = new InputStreamReader(input, encoding);
        write(in, output);
    }

    public static void write(Reader input, Writer output) throws IOException {
        int len;
        char[] buffer = new char[4096];
        while (-1 != (len = input.read(buffer)))
            output.write(buffer, 0, len);
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        input1 = toBufferedInputStream(input1);
        input2 = toBufferedInputStream(input2);

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return ch2 == -1;
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        input1 = toBufferedReader(input1);
        input2 = toBufferedReader(input2);

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return ch2 == -1;
    }

    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
        BufferedReader br1 = toBufferedReader(input1);
        BufferedReader br2 = toBufferedReader(input2);

        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while ((line1 != null) && (line2 != null) && (line1.equals(line2))) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        return line1 != null && (line2 == null || line1.equals(line2));
    }

    /**
     * Access to a directory available size.
     *
     * @param path path.
     * @return space size.
     */
    public static long getDirSize(String path) {
        StatFs stat;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            return getStatFsSize(stat, "getBlockSizeLong", "getAvailableBlocksLong");
        } else {
            return getStatFsSize(stat, "getBlockSize", "getAvailableBlocks");
        }
    }

    private static long getStatFsSize(StatFs statFs, String blockSizeMethod, String availableBlocksMethod) {
        try {
            Method getBlockSizeMethod = statFs.getClass().getMethod(blockSizeMethod);
            getBlockSizeMethod.setAccessible(true);

            Method getAvailableBlocksMethod = statFs.getClass().getMethod(availableBlocksMethod);
            getAvailableBlocksMethod.setAccessible(true);

            long blockSize = (Long) getBlockSizeMethod.invoke(statFs);
            long availableBlocks = (Long) getAvailableBlocksMethod.invoke(statFs);
            return blockSize * availableBlocks;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * If the folder can be written.
     *
     * @param path path.
     * @return True: success, or false: failure.
     */
    public static boolean canWrite(String path) {
        return new File(path).canWrite();
    }

    /**
     * If the folder can be read.
     *
     * @param path path.
     * @return True: success, or false: failure.
     */
    public static boolean canRead(String path) {
        return new File(path).canRead();
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(String folderPath) {
        if (!TextUtils.isEmpty(folderPath)) {
            File folder = new File(folderPath);
            return createFolder(folder);
        }
        return false;
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) {
                return true;
            }
            //noinspection ResultOfMethodCallIgnored
            targetFolder.delete();
        }
        return targetFolder.mkdirs();
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createNewFolder(String folderPath) {
        return delFileOrFolder(folderPath) && createFolder(folderPath);
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createNewFolder(File targetFolder) {
        return delFileOrFolder(targetFolder) && createFolder(targetFolder);
    }

    /**
     * Create a file, If the file exists is not created.
     *
     * @param filePath file path.
     * @return True: success, or false: failure.
     */
    public static boolean createFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return createFile(file);
        }
        return false;
    }

    /**
     * Create a file, If the file exists is not created.
     *
     * @param targetFile file.
     * @return True: success, or false: failure.
     */
    public static boolean createFile(File targetFile) {
        if (targetFile.exists()) {
            if (targetFile.isFile()) {
                return true;
            }
            delFileOrFolder(targetFile);
        }
        try {
            return targetFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Create a new file, if the file exists, delete and create again.
     *
     * @param filePath file path.
     * @return True: success, or false: failure.
     */
    public static boolean createNewFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return createNewFile(file);
        }
        return false;
    }

    /**
     * Create a new file, if the file exists, delete and create again.
     *
     * @param targetFile file.
     * @return True: success, or false: failure.
     */
    public static boolean createNewFile(File targetFile) {
        if (targetFile.exists()) {
            delFileOrFolder(targetFile);
        }
        try {
            return targetFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Delete file or folder.
     *
     * @param path path.
     * @return is succeed.
     * @see #delFileOrFolder(File)
     */
    public static boolean delFileOrFolder(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return delFileOrFolder(new File(path));
    }

    /**
     * Delete file or folder.
     *
     * @param file file.
     * @return is succeed.
     * @see #delFileOrFolder(String)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean delFileOrFolder(File file) {
        if (file == null || !file.exists()) {
            // do nothing
        } else if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File sonFile : files) {
                    delFileOrFolder(sonFile);
                }
            }
            file.delete();
        }
        return true;
    }
}
