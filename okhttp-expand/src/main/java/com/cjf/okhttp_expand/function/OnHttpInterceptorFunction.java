package com.cjf.okhttp_expand.function;


import java.util.List;

import okhttp3.Interceptor;

/**
 * <p>Title: OnHttpInterceptorFunction </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2019/12/18 22:59
 */
public interface OnHttpInterceptorFunction {

    List<Interceptor> getInterceptorList();
}
