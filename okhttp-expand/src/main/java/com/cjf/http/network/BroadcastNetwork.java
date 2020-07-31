package com.cjf.http.network;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cjf.http.function.ContextFunction;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: BroadcastNetwork </p>
 * <p>Description:  网络连接通过广播获取</p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/1 16:35
 */
public class BroadcastNetwork implements Network,
                                         ContextFunction {

    @NonNull
    private final NetworkReceiver mReceiver;

    @NonNull
    private final ContextFunction mContextFunction;

    public BroadcastNetwork(@NonNull final ContextFunction contextFunction) {
        this.mContextFunction = contextFunction;
        this.mReceiver = new NetworkReceiver(new NetworkChecker(contextFunction.getApplication()));

        @NonNull final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction("android.net.ethernet.STATE_CHANGE");
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getApplication().registerReceiver(mReceiver, filter);
    }

    @Override
    public boolean isNetAvailable() {
        return mReceiver.mAvailable;
    }

    @Override
    public void setCheckNetType(NetworkType netType) {
        mReceiver.mNetType = netType;
    }

    @Override
    public void addNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener) {
        if (mReceiver.mOnNetWorkChangedListeners == null) {
            mReceiver.mOnNetWorkChangedListeners = new ArrayList<>();
        }
        if (!mReceiver.mOnNetWorkChangedListeners.contains(listener)) {
            mReceiver.mOnNetWorkChangedListeners.add(listener);
        }
    }

    @Override
    public void removeNetWorkChangedListener(@NonNull final OnNetWorkChangedListener listener) {
        if (mReceiver.mOnNetWorkChangedListeners == null || mReceiver.mOnNetWorkChangedListeners.isEmpty()) {
            return;
        }
        mReceiver.mOnNetWorkChangedListeners.remove(listener);
    }

    @Override
    public void removeAll() {
        if (mReceiver.mOnNetWorkChangedListeners != null) {
            mReceiver.mOnNetWorkChangedListeners.clear();
        }
        mReceiver.mOnNetWorkChangedListeners = null;
    }

    @Override
    public void onDestroy() {
        getApplication().unregisterReceiver(mReceiver);
        removeAll();
    }

    @Override
    @NonNull
    public Application getApplication() {
        return mContextFunction.getApplication();
    }

    private static class NetworkReceiver extends BroadcastReceiver {
        @NonNull
        private final NetworkChecker mChecker;
        private boolean mAvailable;
        private NetworkType mNetType = NetworkType.Default;
        @Nullable
        private List<OnNetWorkChangedListener> mOnNetWorkChangedListeners;


        public NetworkReceiver(@NonNull final NetworkChecker checker) {
            this.mChecker = checker;
            this.mAvailable = mChecker.isAvailable();
        }

        @Override
        public void onReceive(@NonNull Context context, final Intent intent) {
            if (NetworkType.Default.equals(mNetType)) {
                mAvailable = mChecker.isAvailable();
            } else {
                mAvailable = mChecker.isAvailable(mNetType.getType());
            }
            if (mOnNetWorkChangedListeners == null) {
                return;
            }
            for (OnNetWorkChangedListener listener : mOnNetWorkChangedListeners) {
                listener.onNetWorkChanged(mChecker);
            }
        }
    }
}
