package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.zkoss.builder.paratemters.AnnotationParameter;
import org.zkoss.zk.ui.sys.ComponentCtrl;

/**
 * 组件注解构建类
 *
 * @author JunJie
 */
public class AnnotationBuilder {

    /**
     * 为组件设置注解
     *
     * @param ctrl      组件控制对象
     * @param parameter 组件注解参数对象
     */
    public static void addAnnotation(ComponentCtrl ctrl, AnnotationParameter parameter) {
        addAnnotations(ctrl, new AnnotationParameter[] { parameter });
    }

    /**
     * 为组件设置一组注解
     *
     * @param ctrl       组件控制对象
     * @param parameters 组件注解参数对象集合
     */
    public static void addAnnotations(ComponentCtrl ctrl, AnnotationParameter[] parameters) {
        for (AnnotationParameter parameter : parameters) {
            ctrl.addAnnotation(
                parameter.getProperty(),
                parameter.getAnnotateName(),
                parameter.getParameters()
            );
        }
    }
}
