package com.googlecode.easyec.zkoss.builder.paratemters;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.ArrayUtils.addAll;

/**
 * 组件注解构建器参数类
 */
public class AnnotationParameter {

    private String property;
    private String annotateName;
    private Map<String, String[]> parameters = new HashMap<String, String[]>();

    private AnnotationParameter(String property, String annotateName) {
        this.property = property;
        this.annotateName = annotateName;
    }

    public String getProperty() {
        return property;
    }

    public String getAnnotateName() {
        return annotateName;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    /**
     * 参数构建类
     *
     * @author JunJie
     */
    public static final class ParameterBuilder {

        private AnnotationParameter parameter;

        /**
         * ZK组件注解参数构造方法
         *
         * @param property     组件的属性名称
         * @param annotateName 组件注解注入名称
         */
        public ParameterBuilder(String property, String annotateName) {
            Assert.notNull(property, "Property cannot be null.");
            Assert.notNull(annotateName, "Annotation name cannot be null.");

            parameter = new AnnotationParameter(property, annotateName);
        }

        /**
         * 设置组件注解的参数集合
         *
         * @param params 参数集合
         * @return 返回当前参数构建器实例
         */
        public ParameterBuilder parameterMap(Map<String, String[]> params) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                addOrMergeParameters(key, params.get(key));
            }

            return this;
        }

        /**
         * 设置组件注解默认的参数，默认的键是value。
         *
         * @param values 参数集合
         * @return 返回当前参数构建器实例
         */
        public ParameterBuilder parameter(String... values) {
            parameters("value", values);

            return this;
        }

        /**
         * 设置组件注解的参数
         *
         * @param key    参数的键
         * @param values 参数的值集合
         * @return 返回当前参数构建器实例
         */
        public ParameterBuilder parameters(String key, String... values) {
            addOrMergeParameters(key, values);

            return this;
        }

        /**
         * 返回注解参数构建的实例对象
         *
         * @return <code>AnnotationParameter</code>对象
         */
        public AnnotationParameter build() {
            return parameter;
        }

        private void addOrMergeParameters(String key, String[] params) {
            if (parameter.parameters.containsKey(key)) {
                String[] olds = parameter.parameters.get(key);
                parameter.parameters.put(key, (String[]) addAll(olds, params));
            } else {
                parameter.parameters.put(key, params);
            }
        }
    }
}
