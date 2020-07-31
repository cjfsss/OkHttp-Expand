package com.cjf.http;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.encrypt.EncryptFunction;
import com.cjf.http.exception.OkHttpNullException;
import com.cjf.http.function.ContextFunction;
import com.cjf.http.function.OkHttpClientBuilderFunction;
import com.cjf.http.function.OnHttpInterceptorFunction;
import com.cjf.http.interceptor.ConnectInterceptor;
import com.cjf.http.interceptor.EncryptInterceptor;
import com.cjf.http.interceptor.HttpLoggingInterceptor;
import com.cjf.http.network.BroadcastNetwork;
import com.cjf.http.network.Network;
import com.cjf.http.network.NetworkType;
import com.cjf.http.network.OnNetWorkChangedListener;
import com.cjf.http.ssl.SSLSocketFactoryImpl;
import com.cjf.http.ssl.X509TrustManagerImpl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Title: OkHttpFactory </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:57
 */
public final class OkHttpFactory implements Network,
                                            ContextFunction {
    @Nullable
    private Network mNetWork;
    @Nullable
    private OnHttpInterceptorFunction mOnHttpInterceptor;
    @Nullable
    private OkHttpClient mOkHttpClient;
    @Nullable
    private ContextFunction mContextFunction;
    @Nullable
    private EncryptFunction mEncryptFunction;
    @Nullable
    private OkHttpClientBuilderFunction mOkHttpClientBuilderFunction;

    private OkHttpFactory() {
    }

    @NonNull
    public static OkHttpFactory getInstance() {
        return OkHttpFactoryHolder.getSingletonManager();
    }

    private static final class OkHttpFactoryHolder {

        private volatile static OkHttpFactory singletonManager;

        static OkHttpFactory getSingletonManager() {
            if (singletonManager == null) {
                synchronized (OkHttpFactoryHolder.class) {
                    if (singletonManager == null) {
                        return singletonManager = new OkHttpFactory();
                    }
                }
            }
            return singletonManager;
        }
    }

    @NonNull
    public OkHttpFactory init(@NonNull final ContextFunction contextFunction) {
        mContextFunction = contextFunction;
        return this;
    }

    @NonNull
    public OkHttpFactory init(@NonNull final ContextFunction contextFunction,
            @NonNull final OnHttpInterceptorFunction onHttpInterceptor) {
        mContextFunction = contextFunction;
        mOnHttpInterceptor = onHttpInterceptor;
        return this;
    }

    @NonNull
    public OkHttpFactory init(@NonNull final ContextFunction contextFunction,
            @NonNull final OnHttpInterceptorFunction onHttpInterceptor,
            @NonNull final OkHttpClientBuilderFunction function) {
        mContextFunction = contextFunction;
        mOkHttpClientBuilderFunction = function;
        mOnHttpInterceptor = onHttpInterceptor;
        return this;
    }

    @NonNull
    public OkHttpFactory setEncrypt(@NonNull EncryptFunction encryptFunction) {
        mEncryptFunction = encryptFunction;
        return this;
    }

    @NonNull
    public OkHttpFactory interceptor(@NonNull final OnHttpInterceptorFunction onHttpInterceptor) {
        mOnHttpInterceptor = onHttpInterceptor;
        return this;
    }

    @NonNull
    public OkHttpClient getOkHttpClient() {
        return getOkHttpClient(mOkHttpClientBuilderFunction);
    }

    @NonNull
    public OkHttpClient getOkHttpClient(@Nullable OkHttpClientBuilderFunction function) {
        if (function == null) {
            function = mOkHttpClientBuilderFunction = new OkHttpClientBuilderFunction() {
                @Override
                public OkHttpClient.Builder getOkHttpClientBuilder(OkHttpClient.Builder builder) {
                    return builder;
                }
            };
        }
        if (mContextFunction == null) {
            throw new OkHttpNullException("ContextFunction must be not null");
        }
        if (mOkHttpClient == null) {
            mOkHttpClient = function.getOkHttpClientBuilder(getOkHttpClientBuilder()).build();
        }
        return mOkHttpClient;
    }

    @NonNull
    private OkHttpClient.Builder getOkHttpClientBuilder() {
        @NonNull final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        @Nullable final Interceptor encryptInterceptor = getEncryptInterceptor();
        boolean containEncrypt = true;
        if (mOnHttpInterceptor != null) {
            @Nullable final List<Interceptor> interceptorList = mOnHttpInterceptor.getInterceptorList();
            if (interceptorList != null && !interceptorList.isEmpty()) {
                containEncrypt = !interceptorList.contains(encryptInterceptor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    interceptorList.forEach(builder::addInterceptor);
                } else {
                    for (Interceptor interceptor : interceptorList) {
                        builder.addInterceptor(interceptor);
                    }
                }
            }
            mOnHttpInterceptor = null;
        }
        if (isApkInDebug()) {
            builder.addInterceptor(getLogInterceptor());
        }
        if (encryptInterceptor != null && containEncrypt) {
            builder.addInterceptor(encryptInterceptor);
            mEncryptFunction = null;
        }
        @NonNull final X509TrustManager trustAllCert = new X509TrustManagerImpl();
        @NonNull final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryImpl(trustAllCert);
        return builder.addNetworkInterceptor(getConnectInterceptor()).connectTimeout(10, TimeUnit.SECONDS)
                      .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                      .sslSocketFactory(sslSocketFactory, trustAllCert) //添加信任证书
                      .hostnameVerifier((hostname, session) -> true) //忽略host验证
                      .retryOnConnectionFailure(false);
    }

    @NonNull
    public Call newCall(@NonNull Request request) {
        return getOkHttpClient().newCall(request);
    }

    @NonNull
    public Response execute(@NonNull Request request) throws IOException {
        return newCall(request).execute();
    }

    @NonNull
    public Call execute(@NonNull Request request, @NonNull Callback callback) {
        @NonNull final Call call = newCall(request);
        call.enqueue(callback);
        return call;
    }

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        @NonNull final OkHttpClient okHttpClient = getOkHttpClient();
        okHttpClient.dispatcher().cancelAll();
    }

    @NonNull
    public Application getApplication() {
        if (mContextFunction == null) {
            throw new OkHttpNullException("ContextFunction must be not null");
        }
        return mContextFunction.getApplication();
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        final OkHttpClient okHttpClient = getOkHttpClient();
        Dispatcher dispatcher = okHttpClient.dispatcher();

        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 获取日志拦截器
     *
     * @return
     */
    @NonNull
    private HttpLoggingInterceptor getLogInterceptor() {
        @NonNull final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkHttp");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor
                .setPrintLevel(isApkInDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        return loggingInterceptor;
    }

    @Nullable
    private Interceptor getEncryptInterceptor() {
        if (mEncryptFunction == null) {
            return null;
        }
        return new EncryptInterceptor(mEncryptFunction);
    }

    /**
     * 获取连接拦截器
     *
     * @return
     */
    @NonNull
    private ConnectInterceptor getConnectInterceptor() {
        return new ConnectInterceptor(getNetWork());
    }

    /**
     * 获取网络连接
     *
     * @return
     */
    @NonNull
    public Network getNetWork() {
        if (mContextFunction == null) {
            throw new OkHttpNullException("ContextFunction must be not null");
        }
        if (mNetWork == null) {
            return mNetWork = new BroadcastNetwork(mContextFunction);
        }
        return mNetWork;
    }

    /**
     * 判断网络连接是否连通
     *
     * @return true 连通
     */
    public boolean isNetAvailable() {
        return getNetWork().isNetAvailable();
    }

    @Override
    public void setCheckNetType(NetworkType netType) {
        getNetWork().setCheckNetType(netType);
    }

    @Override
    public void addNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener) {
        getNetWork().addNetWorkChangedListener(listener);
    }

    @Override
    public void removeNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener) {
        getNetWork().removeNetWorkChangedListener(listener);
    }

    @Override
    public void removeAll() {
        if (mNetWork != null) {
            mNetWork.removeAll();
        }
    }

    public void onDestroy() {
        if (mNetWork != null) {
            mNetWork.onDestroy();
        }
    }

    /**
     * 2      * 判断当前应用是否是debug状态
     * 3
     */
    public boolean isApkInDebug() {
        try {
            @NonNull final ApplicationInfo info = getApplication().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
