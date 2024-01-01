package com.roadjava.skywalking.agent.demo.apm.core.plugin.loader;

/**
 * 用于加载插件里面的拦截器
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class InterceptorInstanceLoader {
    /**
     *
     * @param interceptorName 插件中拦截器的全类名
     * @param targetClassLoader 要想在插件拦截器中能够访问到被拦截的类,
     *                          需要是同一个类加载器或子类类加载器
     *                          被拦截的类: A - C1
     * @return ConstructorInterceptor 或 InstanceMethodsAroundInterceptor 或
     *          StaticMethodsAroundInterceptor 的实例
     */
    public static <T> T load(String interceptorName,ClassLoader targetClassLoader)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (targetClassLoader == null) {
            targetClassLoader = InterceptorInstanceLoader.class.getClassLoader();
        }
        AgentClassLoader classLoader = new AgentClassLoader(targetClassLoader);
        Object o = Class.forName(interceptorName, true, classLoader).newInstance();
        return (T)o;
    }
}
