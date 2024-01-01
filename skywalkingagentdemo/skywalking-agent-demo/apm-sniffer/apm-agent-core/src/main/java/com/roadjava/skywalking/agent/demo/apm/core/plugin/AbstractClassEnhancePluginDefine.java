package com.roadjava.skywalking.agent.demo.apm.core.plugin;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.StaticMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassMatch;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;

/**
 * 是所有插件的顶级父类
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public abstract class AbstractClassEnhancePluginDefine {

    /**
     * 为匹配到的字节码新增的新属性名称
     */
    public static final String CONTEXT_ATTR_NAME = "_$EnhancedClassField_ws";

    /**
     * 获取当前插件要增强的类
     */
    protected abstract ClassMatch enhanceClass();

    /**
     * 获取实例方法的拦截点
     */
    protected abstract InstanceMethodsInterceptorPoint[] getInstanceMethodsInterceptorPoints();

    /**
     * 获取构造方法的拦截点
     */
    protected abstract ConstructorMethodsInterceptorPoint[] getConstructorMethodsInterceptorPoints();

    /**
     * 获取实例方法的拦截点
     */
    protected abstract StaticMethodsInterceptorPoint[] getStaticMethodsInterceptorPoints();

    /**
     * 增强类的主入口
     */
    public DynamicType.Builder<?> define(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader, EnhanceContext context) {
        // com.roadjava.skywalking.agent.demo.apm.plugins.springmvc.RestControllerInstrumentation
        String pluginDefineClassName = this.getClass().getName();
        // com.roadjava.skywalking.agent.demo.app.controller.UserInfoController
        String typeName = typeDescription.getTypeName();
        log.debug("开始使用{}增强{}",pluginDefineClassName,typeName);
        DynamicType.Builder<?> newBuilder = this.enhance(typeDescription,builder,classLoader,context);

        // 设置为已进行增强处理
        context.initializationStageCompleted();
        log.debug("使用{}增强{}结束",pluginDefineClassName,typeName);
        return newBuilder;
    }


    private DynamicType.Builder<?> enhance(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader, EnhanceContext context) {
        builder = this.enhanceClass(typeDescription,builder,classLoader);
        builder = this.enhanceInstance(typeDescription,builder,classLoader,context);
        return builder;
    }

    /**
     * 增强静态方法
     */
    protected abstract DynamicType.Builder<?> enhanceClass(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader);

    /**
     * 增强实例方法和构造方法
     */
    protected abstract DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader, EnhanceContext context);
}
