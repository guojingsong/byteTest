package com.roadjava.skywalking.agent.demo.apm.agent;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.AbstractClassEnhancePluginDefine;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.EnhanceContext;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.PluginBootstrap;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.PluginFinder;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static net.bytebuddy.matcher.ElementMatchers.nameEndsWith;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class SkyWalkingAgent {
    public static void premain(String args, Instrumentation instrumentation) {

        log.info("进入到premain,args:{}",args);
        PluginFinder pluginFinder;
        try {
            pluginFinder = new PluginFinder(new PluginBootstrap().loadPlugins());
        }catch (Exception e) {
            log.error("初始化失败",e);
            return;
        }

        ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(true));
        AgentBuilder builder = new AgentBuilder.Default(byteBuddy);
                /*
                    // springmvc
                    isAnnotatedWith(named(CONTROLLER_NAME).or(named(REST_CONTROLLER_NAME)))
                    // mysql
                    .or(named(CLIENT_PS_NAME).or(named(SERVER_PS_NAME)))
                    // es
                    .or(xx)
                 */
        builder.type(pluginFinder.buildMatch())
                .transform(new Transformer(pluginFinder))
                .installOn(instrumentation);
    }

    private static class Transformer implements AgentBuilder.Transformer{

        private PluginFinder pluginFinder;

        public Transformer(PluginFinder pluginFinder) {
            this.pluginFinder = pluginFinder;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                // 加载 typeDescription这个类的类加载器
                                                ClassLoader classLoader,
                                                JavaModule module) {
            log.info("actualName to transform:{}", typeDescription.getActualName());
            List<AbstractClassEnhancePluginDefine> pluginDefines = pluginFinder.find(typeDescription);
            if (pluginDefines.size() > 0) {
                DynamicType.Builder<?> newBuilder = builder;
                EnhanceContext context = new EnhanceContext();
                for (AbstractClassEnhancePluginDefine pluginDefine : pluginDefines) {
                    DynamicType.Builder<?> possibleNewBuilder = pluginDefine.define(typeDescription,newBuilder,classLoader,context);
                    if (possibleNewBuilder != null) {
                        newBuilder = possibleNewBuilder;
                    }
                }
                if (context.isEnhanced()) {
                    log.debug("finish the enhance for {}",typeDescription.getTypeName());
                }
                return newBuilder;
            }
            log.debug("匹配到了类:{},但是未find到插件集合",typeDescription.getActualName());
            return builder;

//            DynamicType.Builder<?> newBuilder = builder
                    /*
                        // springmvc
                        not(isStatic()).and(isAnnotatedWith(
                                nameStartsWith(MAPPING_PKG_PREFIX).and(nameEndsWith(MAPPING_SUFFIX))
                        ))
                        // mysql
                        .or(named("execute")
                                .or(named("executeUpdate"))
                                .or(named("executeQuery")))
                        // es
                        .or(xx)
                     */
//                    .method()
                    /*
                        // springmvc
                        new SpringMvcInterceptor()
                        // mysql
                        new MysqlInterceptor()
                        // es
                        new EsInterceptor()
                     */
//                    .intercept(MethodDelegation.to(null));
//            return newBuilder;
        }
    }
}
