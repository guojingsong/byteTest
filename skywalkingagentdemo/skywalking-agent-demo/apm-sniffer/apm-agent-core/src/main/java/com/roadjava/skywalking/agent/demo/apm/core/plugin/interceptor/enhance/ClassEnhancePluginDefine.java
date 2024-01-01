package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.AbstractClassEnhancePluginDefine;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.EnhanceContext;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.InstanceMethodsInterceptorPoint;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.StaticMethodsInterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * 所有的插件都必须直接或间接继承此类,此类完成了增强(transform里指定method和intercept):
 * DynamicType.Builder<?> newBuilder = builder.method(xx).intercept(MethodDelegation.to(xx))
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public abstract class ClassEnhancePluginDefine extends AbstractClassEnhancePluginDefine {
    /**
     * 增强静态的方法
     */
    @Override
    protected DynamicType.Builder<?> enhanceClass(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader) {
        StaticMethodsInterceptorPoint[] staticMethodsInterceptorPoints = getStaticMethodsInterceptorPoints();
        if (staticMethodsInterceptorPoints == null || staticMethodsInterceptorPoints.length == 0) {
            return builder;
        }
        String typeName = typeDescription.getTypeName();
        for (StaticMethodsInterceptorPoint staticMethodsInterceptorPoint : staticMethodsInterceptorPoints) {
            String methodsInterceptor = staticMethodsInterceptorPoint.getMethodsInterceptor();
            if (methodsInterceptor == null || "".equals(methodsInterceptor.trim())) {
                throw new RuntimeException("要增强的类"+typeName+"没有指定拦截器");
            }
            ElementMatcher<MethodDescription> methodsMatcher = staticMethodsInterceptorPoint.getMethodsMatcher();

            builder = builder.method(isStatic().and(methodsMatcher))
                    .intercept(MethodDelegation.withDefaultConfiguration()
                            .to(new StaticMethodsInter(methodsInterceptor,classLoader)));
        }
        return builder;
    }

    /**
     * 增强实例方法和构造方法
     */
    @Override
    protected DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription, DynamicType.Builder<?> builder, ClassLoader classLoader, EnhanceContext context) {
        ConstructorMethodsInterceptorPoint[] constructorMethodsInterceptorPoints = getConstructorMethodsInterceptorPoints();
        InstanceMethodsInterceptorPoint[] instanceMethodsInterceptorPoints = getInstanceMethodsInterceptorPoints();
        // 构造拦截点是否存在
        boolean existConstructorInterceptorPoint = false;
        if (constructorMethodsInterceptorPoints != null && constructorMethodsInterceptorPoints.length > 0) {
            existConstructorInterceptorPoint = true;
        }
        // 实例方法拦截点是否存在
        boolean existInstanceMethodInterceptorPoint = false;
        if (instanceMethodsInterceptorPoints != null && instanceMethodsInterceptorPoints.length > 0) {
            existInstanceMethodInterceptorPoint = true;
        }
        if (!existConstructorInterceptorPoint && !existInstanceMethodInterceptorPoint) {
            return builder;
        }
        /*
        为字节码新增属性,对于同一个typeDescription只需要执行一次
         */
        // typeDescription 不是 EnhancedInstance的实现类为真
        if (!typeDescription.isAssignableTo(EnhancedInstance.class)) {
            if (!context.isObjectExtended()) {
                builder = builder.defineField(CONTEXT_ATTR_NAME
                        ,Object.class
                        , Opcodes.ACC_PRIVATE | Opcodes.ACC_VOLATILE)
                        // 指定属性的getter 和 setter
                        .implement(EnhancedInstance.class)
                        .intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME));
               // 修改context为已扩展
               context.objectExtendedCompleted();
            }
        }


        String typeName = typeDescription.getTypeName();
        // 增强构造方法
        if (existConstructorInterceptorPoint) {
            for (ConstructorMethodsInterceptorPoint constructorMethodsInterceptorPoint : constructorMethodsInterceptorPoints) {
                String constructorInterceptor = constructorMethodsInterceptorPoint.getConstructorInterceptor();
                if (constructorInterceptor == null || "".equals(constructorInterceptor.trim())) {
                    throw new RuntimeException("要增强的类"+typeName+"没有指定拦截器");
                }
                ElementMatcher<MethodDescription> constructorMatcher = constructorMethodsInterceptorPoint.getConstructorMatcher();

                builder = builder.constructor(constructorMatcher)
                        .intercept(SuperMethodCall.INSTANCE.andThen(
                                MethodDelegation.withDefaultConfiguration()
                                        .to(new ConstructorInter(constructorInterceptor,classLoader))
                        ));
            }
        }
        // 增强实例方法
        if (existInstanceMethodInterceptorPoint) {
            for (InstanceMethodsInterceptorPoint instanceMethodsInterceptorPoint : instanceMethodsInterceptorPoints) {
                String methodsInterceptor = instanceMethodsInterceptorPoint.getMethodsInterceptor();
                if (methodsInterceptor == null || "".equals(methodsInterceptor.trim())) {
                    throw new RuntimeException("要增强的类"+typeName+"没有指定拦截器");
                }
                ElementMatcher<MethodDescription> methodsMatcher = instanceMethodsInterceptorPoint.getMethodsMatcher();

                builder = builder.method(not(isStatic()).and(methodsMatcher))
                        .intercept(MethodDelegation.withDefaultConfiguration()
                                .to(new InstMethodsInter(methodsInterceptor,classLoader)));
            }
        }

        return builder;
    }
}
