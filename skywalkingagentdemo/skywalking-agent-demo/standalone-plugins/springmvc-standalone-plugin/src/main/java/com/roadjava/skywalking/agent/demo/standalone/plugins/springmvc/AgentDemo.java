package com.roadjava.skywalking.agent.demo.standalone.plugins.springmvc;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * 实现的功能: 拦截springmvc的controller方法并统计耗时
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class AgentDemo {
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";
    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";
    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到premain,args:{}",args);
        AgentBuilder builder = new AgentBuilder.Default()
                .type(isAnnotatedWith(named(CONTROLLER_NAME).or(named(REST_CONTROLLER_NAME))))
                .transform(new AgentTransformer());
        builder.installOn(instrumentation);
    }
}
