package com.roadjava.skywalking.agent.demo.apm.core.plugin.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 多个类名相等匹配器
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class MultiClassNameMatch implements IndirectMatch {
    /**
     * 要匹配的类名称
     */
    private List<String> needMatchClassNames;

    private MultiClassNameMatch(String[] classNames) {
        if (classNames == null || classNames.length == 0) {
            throw new IllegalArgumentException("needMatchClassNames can not be null");
        }
        this.needMatchClassNames = Arrays.asList(classNames);
    }

    /**
     * 多个类要求是or的关系
     * @return
     */
    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<? super TypeDescription> junction = null;
        for (String needMatchClassName : needMatchClassNames) {
            if (junction == null) {
                junction = named(needMatchClassName);
            } else {
                junction = junction.or(named(needMatchClassName));
            }
        }
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        /*
        比如needMatchClassNames里面是com.mysql.cj.jdbc.ServerPreparedStatement、
        com.mysql.cj.jdbc.ClientPreparedStatement
        而typeDescription是a.b.C
         */
        return needMatchClassNames.contains(typeDescription.getTypeName());
    }

    public static IndirectMatch byMultiClassMatch(String... classNames) {
        return new MultiClassNameMatch(classNames);
    }
}
