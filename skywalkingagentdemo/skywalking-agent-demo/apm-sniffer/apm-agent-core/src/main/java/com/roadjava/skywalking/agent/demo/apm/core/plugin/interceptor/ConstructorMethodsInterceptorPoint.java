package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 构造方法拦截点
 */
public interface ConstructorMethodsInterceptorPoint {

    /**
     * 要拦截哪些方法
     * @return 作为method()方法的参数
     */
    ElementMatcher<MethodDescription> getConstructorMatcher();

    /**
     * 获取被增强方法对应的拦截器,该拦截器必须实现ConstructorInterceptor接口
     */
    String getConstructorInterceptor();
}
