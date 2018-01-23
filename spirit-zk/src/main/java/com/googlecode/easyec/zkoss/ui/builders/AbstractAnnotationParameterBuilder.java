package com.googlecode.easyec.zkoss.ui.builders;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * ZK 组件注解的参数的基础构建类
 *
 * @author junjie
 */
public abstract class AbstractAnnotationParameterBuilder {

    private String property;
    private String annotateName;
    private Map<String, String[]> args = new HashMap<>();

    public void setProperty(String property) {
        this.property = property;
    }

    public void setAnnotateName(String annotateName) {
        this.annotateName = annotateName;
    }

    public void setValue(String value) {
        setValues(value);
    }

    public void setValues(String... values) {
        this.args.put("value", values);
    }

    public void addArg(String k, String... v) {
        addOrMerge(k, v);
    }

    protected void addOrMerge(String k, String[] v) {
        if (StringUtils.isNotBlank(k) && v != null) {
            String[] oldValues = args.get(k);
            if (oldValues == null) {
                args.put(k, v);
            } else {
                args.put(k, ArrayUtils.addAll(oldValues, v));
            }
        }
    }

    protected String getProperty() {
        return property;
    }

    protected String getAnnotateName() {
        return annotateName;
    }

    protected Map<String, String[]> getArgs() {
        return args;
    }

    abstract public AnnotationParameter build();

    protected static class DefaultAnnotationParameter extends AnnotationParameter {


        protected DefaultAnnotationParameter(String property, String annotateName) {
            super(property, annotateName);
        }
    }
}
