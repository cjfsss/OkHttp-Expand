package com.cjf.http.function;

import android.app.Application;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * <p>Title: ContextFunction </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/27 17:47
 */
public interface ContextFunction {

    @NonNull
    Application getApplication();
}
