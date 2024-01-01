package com.roadjava.skywalking.agent.demo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * java -javaagent:E:\programs2\skywalking\agent\skywalking-agent\skywalking-agent.jar -Dskywalking.agent.service_name=abc  -jar xx.jar
 * 单独生效:
 * -javaagent:E:\ideaProjects2\skywalking-agent-demo\standalone-plugins\springmvc-standalone-plugin\target\springmvc-standalone-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
 * -javaagent:E:\ideaProjects2\skywalking-agent-demo\standalone-plugins\mysql-standalone-plugin\target\mysql-standalone-plugin-1.0-SNAPSHOT-jar-with-dependencies.jar
 * 同时生效:
 * 可以同时指定多个javaagent参数,格式: -javaagent:xx  -javaagent:xx
 * 自己实现的可插拔式的agent.jar
 * -javaagent:E:\ideaProjects2\skywalking-agent-demo\dist\apm-agent-1.0-SNAPSHOT-jar-with-dependencies.jar
 * @author zhaodaowen
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = "com.roadjava.skywalking.agent.demo.app.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
