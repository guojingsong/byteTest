package com.roadjava.skywalking.agent.demo.standalone.plugins.springmvc;

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
    private static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
    private static final String MAPPING_SUFFIX = "Mapping";

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
                        not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                        ))
                ).intercept(MethodDelegation.to(new SpringMvcInterceptor()));
        return intercept;
    }
}
