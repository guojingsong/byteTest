package com.roadjava.skywalking.agent.demo.apm.plugins.springmvc;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.AbstractClassEnhancePluginDefine;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance.ClassEnhancePluginDefine;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.nameEndsWith;

/**
 * springmvc插件公共类
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public abstract class SpringmvcCommonInstrumentation extends ClassEnhancePluginDefine {
    private static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    private static final String MAPPING_SUFFIX = "Mapping";
    private static final String INTERCEPTOR = "com.roadjava.skywalking.agent.demo.apm.plugins.springmvc.interceptor.SpringmvcInterceptor";

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[]{
                new InstanceMethodsInterceptorPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return  not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                        ));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPTOR;
                    }
                }
        };
    }

    @Override
    protected ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints() {
        return null;
    }

    @Override
    protected StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints() {
        return new StaticMethodsInterceptorPoint[0];
    }
}
