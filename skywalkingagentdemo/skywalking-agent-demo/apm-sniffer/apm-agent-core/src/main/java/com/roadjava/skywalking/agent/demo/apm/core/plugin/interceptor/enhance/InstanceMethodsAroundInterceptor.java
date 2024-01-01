package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance;

import java.lang.reflect.Method;

/**
 * 实例方法的intercept必须实现这个接口
 */
public interface InstanceMethodsAroundInterceptor {
    /**
     * 前置通知
     */
    void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes);

    /**
     * 最终通知,不过原方法是否执行异常都会执行
     * @param ret 原方法的执行结果
     */
    Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret);

    /**
     * 异常通知
     */
    void handleEx(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t);

}
