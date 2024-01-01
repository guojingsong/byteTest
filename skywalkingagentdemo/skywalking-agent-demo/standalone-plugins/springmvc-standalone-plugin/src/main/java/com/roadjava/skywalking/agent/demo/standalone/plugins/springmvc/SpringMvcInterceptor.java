package com.roadjava.skywalking.agent.demo.standalone.plugins.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class SpringMvcInterceptor {

    @RuntimeType
    public Object intercept(
            @This Object targetObj,
            @Origin Method targetMethod,
            @AllArguments Object[] targetMethodArgs,
            @SuperCall Callable<?> zuper
            ) {
        log.info("before controller exec,methodName:{},args:{}",targetMethod.getName(),Arrays.toString(targetMethodArgs));
        long start = System.currentTimeMillis();
        Object call = null;
        try {
            call = zuper.call();
            log.info("after controller exec,result:{}",call);
        }catch (Exception e) {
            log.error("controller exec error",e);
        }finally {
            long end = System.currentTimeMillis();
            log.info("finally controller exec in {} ms",end - start);
        }
        return call;
    }
}
