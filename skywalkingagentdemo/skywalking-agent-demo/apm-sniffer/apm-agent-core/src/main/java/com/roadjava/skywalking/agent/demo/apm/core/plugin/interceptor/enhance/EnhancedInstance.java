package com.roadjava.skywalking.agent.demo.apm.core.plugin.interceptor.enhance;

/**
 * 所有需要增强构造或实例方法的字节码都会实现这个接口
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public interface EnhancedInstance {
    Object getSkyWalkingDynamicField();
    void setSkyWalkingDynamicField(Object value);
}
