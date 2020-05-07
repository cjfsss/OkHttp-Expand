package com.cjf.okhttp_expand.network;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.cjf.okhttp_expand.function.ContextFunction;

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
public class BroadcastNetwork implements Network, ContextFunction {

    private final NetworkReceiver mReceiver;

    private final ContextFunction mContextFunction;

    public BroadcastNetwork(final ContextFunction contextFunction) {
        this.mContextFunction = contextFunction;
        this.mReceiver = new NetworkReceiver(new NetworkChecker(contextFunction.getApplication()));

        IntentFilter filter = new IntentFilter();
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
    public void addNetWorkChangedListener(OnNetWorkChangedListener listener) {
        if (mReceiver.mOnNetWorkChangedListeners == null) {
            mReceiver.mOnNetWorkChangedListeners = new ArrayList<>();
        }
        if (!mReceiver.mOnNetWorkChangedListeners.contains(listener)) {
            mReceiver.mOnNetWorkChangedListeners.add(listener);
        }
    }

    @Override
    public void removeNetWorkChangedListener(OnNetWorkChangedListener listener) {
        if (mReceiver.mOnNetWorkChangedListeners == null || mReceiver.mOnNetWorkChangedListeners.isEmpty()) {
            return;
        }
        mReceiver.mOnNetWorkChangedListeners.remove(listener);
    }

    @Override
    public void removeAll() {
        mReceiver.mOnNetWorkChangedListeners.clear();
        mReceiver.mOnNetWorkChangedListeners = null;
    }

    @Override
    public void onDestroy() {
        getApplication().unregisterReceiver(mReceiver);
        removeAll();
    }

    @Override
    public Application getApplication() {
        return mContextFunction.getApplication();
    }

    private static class NetworkReceiver extends BroadcastReceiver {

        private final NetworkChecker mChecker;
        private boolean mAvailable;
        private boolean mAvailableOld;
        private List<OnNetWorkChangedListener> mOnNetWorkChangedListeners;


        public NetworkReceiver(final NetworkChecker checker) {
            this.mChecker = checker;
            this.mAvailable = mChecker.isAvailable();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mAvailable = mChecker.isAvailable();
            if (mAvailable != mAvailableOld) {
                mAvailableOld = mAvailable;
                if (mOnNetWorkChangedListeners == null) {
                    return;
                }
                for (OnNetWorkChangedListener listener : mOnNetWorkChangedListeners) {
                    listener.onNetWorkChanged(mAvailable);
                }
            }
        }
    }
}
