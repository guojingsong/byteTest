package com.roadjava.skywalking.agent.demo.apm.plugins.springmvc;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassAnnotationNameMatch;
import com.roadjava.skywalking.agent.demo.apm.core.plugin.match.ClassMatch;

/**
 * 拦截带有@Controller的springmvc插件
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class ControllerInstrumentation extends SpringmvcCommonInstrumentation {
    private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";

    @Override
    protected ClassMatch enhanceClass() {
        return ClassAnnotationNameMatch.byClassAnnotationMatch(CONTROLLER_NAME);
    }

}
