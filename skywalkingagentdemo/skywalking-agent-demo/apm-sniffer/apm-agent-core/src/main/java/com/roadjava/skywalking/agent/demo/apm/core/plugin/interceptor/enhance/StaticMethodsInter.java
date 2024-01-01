package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.loader.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 静态方法bytebuddy拦截器
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class StaticMethodsInter {
    private StaticMethodsAroundInterceptor interceptor;

    /**
     *
     * @param methodsInterceptor 是StaticMethodsAroundInterceptor的实现类
     * @param classLoader
     */
    public StaticMethodsInter(String methodsInterceptor, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(methodsInterceptor,classLoader);
        }catch (Exception e){
            log.error("can not load ,interceptorName:{}",methodsInterceptor);
        }
    }

    @RuntimeType
    public Object intercept(
            @Origin Class<?> clazz,
            @Origin Method method,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper) throws Throwable{
        // 处理前置通知
        try {
            interceptor.beforeMethod(clazz,method,allArguments,method.getParameterTypes());
        }catch (Throwable e) {
            log.error("class {} before exec static method {} interceptor failure",clazz,method.getName(),e);
        }

        Object call = null;
        try {
            call = zuper.call();
        }catch (Throwable t) {
            // 异常处理
            try {
                interceptor.handleEx(clazz,method,allArguments,method.getParameterTypes(),t);
            }catch (Throwable e) {
                log.error("class {} hanle static method {} exception failure",clazz,method.getName(),e);
            }
            throw t;
        }finally {
            // 最终执行
            try {
                call = interceptor.afterMethod(clazz,method,allArguments,method.getParameterTypes(),call);
            }catch (Throwable t) {
                log.error("class {} after exec static method {} interceptor failure",clazz,method.getName(),t);
            }
        }
        return call;
    }
}
