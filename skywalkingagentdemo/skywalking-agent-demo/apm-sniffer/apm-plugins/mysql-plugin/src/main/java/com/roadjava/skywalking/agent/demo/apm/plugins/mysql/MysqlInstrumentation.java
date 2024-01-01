package com.roadjava.skywalking.agent.demo.apm.plugins.mysql;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.AbstractClassEnhancePluginDefine;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance.ClassEnhancePluginDefine;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassMatch;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.MultiClassNameMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 定义mysql插件
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class MysqlInstrumentation extends ClassEnhancePluginDefine {
    private static final String CLIENT_PS_NAME = "com.mysql.cj.jdbc.ClientPreparedStatement";
    private static final String SERVER_PS_NAME = "com.mysql.cj.jdbc.ServerPreparedStatement";
    private static final String INTERCEPTOR = "com.roadjava.skywalking.agent.demo.apm.plugins.mysql.interceptor.MysqlInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(CLIENT_PS_NAME,SERVER_PS_NAME);
    }

    @Override
    protected InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints() {
        return new InstanceMethodsInterceptorPoint[]{
                new InstanceMethodsInterceptorPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named("execute")
                                .or(named("executeUpdate"))
                                .or(named("executeQuery"));
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
        return new ConstructorMethodsInterceptorPoint[0];
    }

    @Override
    protected StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints() {
        return new StaticMethodsInterceptorPoint[0];
    }
}
