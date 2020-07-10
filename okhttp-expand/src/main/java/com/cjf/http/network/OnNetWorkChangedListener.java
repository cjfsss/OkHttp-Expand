package com.cjf.http.network;

import androidx.annotation.Keep;

/**
 * <p>Title: OnNetWorkChangedListener </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/23 15:33
 */
public interface OnNetWorkChangedListener {

    void onNetWorkChanged(NetworkChecker networkChecker);
}
