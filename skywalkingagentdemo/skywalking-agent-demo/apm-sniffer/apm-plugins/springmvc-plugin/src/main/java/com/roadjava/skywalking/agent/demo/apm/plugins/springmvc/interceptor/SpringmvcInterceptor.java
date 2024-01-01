package com.roadjava.skywalking.agent.demo.apm.plugins.springmvc.interceptor;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance.EnhancedInstance;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class SpringmvcInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        log.info("before springmvc method name:{},args:{}",method.getName(), Arrays.toString(allArguments));
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        log.info("springmvc result:{}",ret);
        return ret;
    }

    @Override
    public void handleEx(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        log.error("springmvc error",t);
    }
}
