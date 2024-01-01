package com.roadjava.skywalking.agent.demo.apm.core.plugin.match;

/**
 * 专门用于类的全限定名称=xx,仅适用于 named(xxx)
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */

public class NameMatch implements ClassMatch {
    private String className;

    private NameMatch(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public static NameMatch byClassName(String className) {
        return new NameMatch(className);
    }

}
