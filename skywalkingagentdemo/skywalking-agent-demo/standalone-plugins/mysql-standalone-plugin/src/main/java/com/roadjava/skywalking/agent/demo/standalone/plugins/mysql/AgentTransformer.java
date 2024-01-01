package com.roadjava.skywalking.agent.demo.standalone.plugins.mysql;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class AgentTransformer implements AgentBuilder.Transformer {

    /**
     * @param typeDescription 表示要被加载的类的信息
     */
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader, // 加载 typeDescription这个类的类加载器
                                            JavaModule module) {
        log.info("actualName to transform:{}", typeDescription.getActualName());
        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder
                .method(
                        named("execute")
                                .or(named("executeUpdate"))
                                .or(named("executeQuery"))
                ).intercept(MethodDelegation.to(new MysqlInterceptor()));
        return intercept;
    }
}
