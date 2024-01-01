package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.loader.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 实例方法bytebuddy拦截器
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class InstMethodsInter {
    private InstanceMethodsAroundInterceptor interceptor;

    /**
     *
     * @param methodsInterceptor 是InstanceMethodsAroundInterceptor的实现类
     * @param classLoader
     */
    public InstMethodsInter(String methodsInterceptor, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(methodsInterceptor,classLoader);
        }catch (Exception e){
            log.error("can not load ,interceptorName:{}",methodsInterceptor);
        }
    }

    @RuntimeType
    public Object intercept(
            @This Object obj,
            @Origin Method method,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper) throws Throwable{
        EnhancedInstance instance = (EnhancedInstance) obj;
        // 处理前置通知
        try {
            interceptor.beforeMethod(instance,method,allArguments,method.getParameterTypes());
        }catch (Throwable e) {
            log.error("class {} before exec instance method {} interceptor failure",obj.getClass(),method.getName(),e);
        }

        Object call = null;
        try {
            call = zuper.call();
        }catch (Throwable t) {
            // 异常处理
            try {
                interceptor.handleEx(instance,method,allArguments,method.getParameterTypes(),t);
            }catch (Throwable e) {
                log.error("class {} hanle instance method {} exception failure",obj.getClass(),method.getName(),e);
            }
            throw t;
        }finally {
            // 最终执行
            try {
                call = interceptor.afterMethod(instance,method,allArguments,method.getParameterTypes(),call);
            }catch (Throwable t) {
                log.error("class {} after exec instance method {} interceptor failure",obj.getClass(),method.getName(),t);
            }
        }
        return call;
    }
}
