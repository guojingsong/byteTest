package com.roadjava.skywalking.agent.demo.standalone.plugins.mysql;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 实现的功能: 拦截mysql的插件
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class AgentDemo {
    private static final String CLIENT_PS_NAME = "com.mysql.cj.jdbc.ClientPreparedStatement";
    private static final String SERVER_PS_NAME = "com.mysql.cj.jdbc.ServerPreparedStatement";
    public static void premain(String args, Instrumentation instrumentation) {
        log.info("进入到premain,args:{}",args);
        AgentBuilder builder = new AgentBuilder.Default()
                .type(named(CLIENT_PS_NAME).or(named(SERVER_PS_NAME)))
                .transform(new AgentTransformer());
        builder.installOn(instrumentation);
    }
}
