package com.roadjava.skywalking.agent.demo.apm.core.plugin.match;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 某个类要同时含有某几个注解匹配器
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class ClassAnnotationNameMatch implements IndirectMatch {
    /**
     * 要包含的注解列表
     */
    private List<String> annotations;

    private ClassAnnotationNameMatch(String[] annotations) {
        if (annotations == null || annotations.length == 0) {
            throw new IllegalArgumentException("annotations can not be null");
        }
        this.annotations = Arrays.asList(annotations);
    }

    /**
     * 多个注解要求是and的关系
     * @return
     */
    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<? super TypeDescription> junction = null;
        for (String anno : annotations) {
            if (junction == null) {
                junction = buildEachAnno(anno);
            } else {
                junction = junction.and(buildEachAnno(anno));
            }
        }
        return junction;
    }

    /**
     *
     * @param typeDescription 待判断的类
     * @return true: annotations是typeDescription上注解的真子集
     */
    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        /*
        annotations: ["a.b.Anno1","a.b.Anno2"]
        typeDescription上的注解: ["a.b.Anno1","a.b.Anno3"]
         */
        ArrayList<String> annotationList = new ArrayList<>(annotations);
        // 获取typeDescription上的注解列表
        AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
        for (AnnotationDescription declaredAnnotation : declaredAnnotations) {
            // 获取注解的全限定名
            String actualName = declaredAnnotation.getAnnotationType().getActualName();
            annotationList.remove(actualName);
        }
        return annotationList.isEmpty();
    }

    private ElementMatcher.Junction<TypeDescription> buildEachAnno(String anno) {
        return isAnnotatedWith(named(anno));
    }

    public static IndirectMatch byClassAnnotationMatch(String... annotations) {
        return new ClassAnnotationNameMatch(annotations);
    }
}
