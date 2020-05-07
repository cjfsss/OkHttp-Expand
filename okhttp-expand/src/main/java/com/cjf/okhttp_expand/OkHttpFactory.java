package com.cjf.okhttp_expand;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.cjf.okhttp_expand.function.ContextFunction;
import com.cjf.okhttp_expand.function.OkHttpClientBuilderFunction;
import com.cjf.okhttp_expand.function.OnHttpInterceptorFunction;
import com.cjf.okhttp_expand.interceptor.ConnectInterceptor;
import com.cjf.okhttp_expand.interceptor.HttpLoggingInterceptor;
import com.cjf.okhttp_expand.network.BroadcastNetwork;
import com.cjf.okhttp_expand.network.Network;
import com.cjf.okhttp_expand.network.OnNetWorkChangedListener;
import com.cjf.okhttp_expand.ssl.SSLSocketFactoryImpl;
import com.cjf.okhttp_expand.ssl.X509TrustManagerImpl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * <p>Title: OkHttpFactory </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:57
 */
public final class OkHttpFactory implements Network, ContextFunction {

    private Network mNetWork;

    private OnHttpInterceptorFunction mOnHttpInterceptor;

    private OkHttpClient mOkHttpClient;

    private ContextFunction mContextFunction;
    private OkHttpClientBuilderFunction mOkHttpClientBuilderFunction;

    private OkHttpFactory() {
    }

    public static OkHttpFactory getInstance() {
        return OkHttpFactoryHolder.getSingletonManager();
    }

    private static final class OkHttpFactoryHolder {

        private volatile static OkHttpFactory singletonManager = null;

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

    public OkHttpFactory init(ContextFunction contextFunction) {
        mContextFunction = contextFunction;
        return this;
    }

    public OkHttpFactory init(ContextFunction contextFunction, OnHttpInterceptorFunction onHttpInterceptor) {
        mContextFunction = contextFunction;
        interceptor(onHttpInterceptor);
        return this;
    }

    public OkHttpFactory init(ContextFunction contextFunction, OnHttpInterceptorFunction onHttpInterceptor,
                              OkHttpClientBuilderFunction function) {
        mContextFunction = contextFunction;
        mOkHttpClientBuilderFunction = function;
        interceptor(onHttpInterceptor);
        return this;
    }

    public OkHttpFactory interceptor(OnHttpInterceptorFunction onHttpInterceptor) {
        mOnHttpInterceptor = onHttpInterceptor;
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return getOkHttpClient(mOkHttpClientBuilderFunction);
    }

    public OkHttpClient getOkHttpClient(OkHttpClientBuilderFunction function) {
        if (function == null) {
            function = mOkHttpClientBuilderFunction = new OkHttpClientBuilderFunction() {
                @Override
                public OkHttpClient.Builder getOkHttpClientBuilder(OkHttpClient.Builder builder) {
                    return builder;
                }
            };
        }
        if (mContextFunction == null) {
            throw new NullPointerException("ContextFunction must be not null");
        }
        if (mOkHttpClient == null) {
            mOkHttpClient = function.getOkHttpClientBuilder(getOkHttpClientBuilder()).build();
        }
        return mOkHttpClient;
    }

    private OkHttpClient.Builder getOkHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (mOnHttpInterceptor != null) {
            final List<Interceptor> interceptorList = mOnHttpInterceptor.getInterceptorList();
            if (interceptorList != null && !interceptorList.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    interceptorList.forEach(builder::addInterceptor);
                } else {
                    for (Interceptor interceptor : interceptorList) {
                        builder.addInterceptor(interceptor);
                    }
                }
            }
        }
        if (isApkInDebug()) {
            builder.addInterceptor(getLogInterceptor());
        }
        final X509TrustManager trustAllCert = new X509TrustManagerImpl();
        final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryImpl(trustAllCert);
        return builder.addNetworkInterceptor(getConnectInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, trustAllCert) //添加信任证书
                .hostnameVerifier((hostname, session) -> true) //忽略host验证
                .retryOnConnectionFailure(false);
    }

    public Application getApplication() {
        if (mContextFunction.getApplication() == null) {
            throw new NullPointerException("ContextFunction.getContext() must be not null");
        }
        return mContextFunction.getApplication();
    }

    /**
     * 获取日志拦截器
     *
     * @return
     */
    private HttpLoggingInterceptor getLogInterceptor() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(isApkInDebug()
                ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        return loggingInterceptor;
    }

    /**
     * 获取连接拦截器
     *
     * @return
     */
    private ConnectInterceptor getConnectInterceptor() {
        return new ConnectInterceptor(getNetWork());
    }

    /**
     * 获取网络连接
     *
     * @return
     */
    private Network getNetWork() {
        if (mContextFunction == null) {
            throw new NullPointerException("ContextFunction must be not null");
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
    public void addNetWorkChangedListener(OnNetWorkChangedListener listener) {
        getNetWork().addNetWorkChangedListener(listener);
    }

    @Override
    public void removeNetWorkChangedListener(OnNetWorkChangedListener listener) {
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
            ApplicationInfo info = getApplication().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
