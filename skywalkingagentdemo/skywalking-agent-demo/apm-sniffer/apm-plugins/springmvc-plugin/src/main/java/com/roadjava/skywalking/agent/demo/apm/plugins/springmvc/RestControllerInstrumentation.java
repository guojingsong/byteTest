package com.roadjava.skywalking.agent.demo.apm.plugins.springmvc;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassAnnotationNameMatch;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassMatch;

/**
 * 拦截带有@RestController的springmvc插件
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class RestControllerInstrumentation extends SpringmvcCommonInstrumentation {
    private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";

    @Override
    protected ClassMatch enhanceClass() {
        return ClassAnnotationNameMatch.byClassAnnotationMatch(REST_CONTROLLER_NAME);
    }
}
