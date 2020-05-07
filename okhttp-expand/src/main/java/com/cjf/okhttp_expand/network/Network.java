package com.cjf.okhttp_expand.network;


/**
 * <p>Title: Network </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 16:21
 */
public interface Network {

    /**
     * The network has always been available.
     */
    Network DEFAULT = new Network() {
        @Override
        public boolean isNetAvailable() {
            return true;
        }

        @Override
        public void addNetWorkChangedListener(OnNetWorkChangedListener listener) {
        }

        @Override
        public void removeNetWorkChangedListener(OnNetWorkChangedListener listener) {
        }

        @Override
        public void removeAll() {
        }

        @Override
        public void onDestroy() {

        }
    };

    /**
     * Check the network is enable.
     */
    boolean isNetAvailable();

    /**
     * 添加网络监听
     */
    void addNetWorkChangedListener(OnNetWorkChangedListener listener);

    /**
     * 移除网络监听
     */
    void removeNetWorkChangedListener(OnNetWorkChangedListener listener);

    /**
     * 移除所有监听
     */
    void removeAll();


    void onDestroy();
}
