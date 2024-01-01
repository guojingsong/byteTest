package com.roadjava.skywalking.agent.demo.apm.core.plugin;

import com.roadjava.skywalking.agent.demo.apm.core.plugin.loader.AgentClassLoader;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
public class PluginResourcesResolver {

    /**
     * 获取插件目录(/plugins)下所有jar内的skywalking-plugin.def文件的url
     * @return
     */
    public List<URL> getResources() {
        List<URL> cfgUrlPaths = new ArrayList<>();
        try {
            Enumeration<URL> urls = AgentClassLoader.getDefault().getResources("skywalking-plugin.def");
            while (urls.hasMoreElements()) {
                URL pluginDefineDefUrl = urls.nextElement();
                cfgUrlPaths.add(pluginDefineDefUrl);
                log.info("find skywalking plugin define file url :{}",pluginDefineDefUrl);
            }
            return cfgUrlPaths;
        }catch (Exception e) {
            log.error("read resource error",e);
        }
        return null;
    }
}
