package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 实例方法拦截点
 */
public interface InstanceMethodsInterceptorPoint {

    /**
     * 要拦截哪些方法
     * @return 作为method()方法的参数
     */
    ElementMatcher<MethodDescription> getMethodsMatcher();

    /**
     * 获取被增强方法对应的拦截器,该拦截器必须实现InstanceMethodsAroundInterceptor接口
     */
    String getMethodsInterceptor();
}
