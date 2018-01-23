package com.googlecode.easyec.zkoss.ui.builders;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持ZK组件Annotation注解的参数类
 *
 * @author junjie
 */
public abstract class AnnotationParameter {

    private String property;
    private String annotateName;
    private Map<String, String[]> args = new HashMap<>();

    public AnnotationParameter(String property, String annotateName) {
        this.property = property;
        this.annotateName = annotateName;
    }

    public String getProperty() {
        return property;
    }

    public String getAnnotateName() {
        return annotateName;
    }

    public Map<String, String[]> getArgs() {
        return args;
    }

    protected void setArgs(Map<String, String[]> args) {
        if (MapUtils.isNotEmpty(args)) {
            this.args.putAll(args);
        }
    }
}
