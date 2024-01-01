package com.roadjava.skywalking.agent.demo.apm.plugins.mysql.interceptor;

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
public class MysqlInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        // sql语句
        objInst.setSkyWalkingDynamicField("select * from user = ?");
        log.info("before mysql method name:{},args:{}",method.getName(), Arrays.toString(allArguments));
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        log.info("mysql result:{}",ret);
        // sql语句发送到oap
        Object skyWalkingDynamicField = objInst.getSkyWalkingDynamicField();
        return ret;
    }

    @Override
    public void handleEx(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        log.error("mysql error",t);
    }
}
