package com.roadjava.skywalking.agent.demo.apm.core.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 所有非NameMatch的情况都要实现IndirectMatch
 */
public interface IndirectMatch extends ClassMatch{
    /**
     * 用于构造type()的参数,比如构建 named(CLIENT_PS_NAME).or(named(SERVER_PS_NAME))
     */
    ElementMatcher.Junction<? super TypeDescription> buildJunction();

    /**
     * 用于判断typeDescription是否满足当前匹配器(IndirectMatch的实现类)的条件
     * @param typeDescription 待判断的类
     * @return true:匹配 false:不匹配
     */
    boolean isMatch(TypeDescription typeDescription);
}
