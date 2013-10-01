package com.googlecode.easyec.zkoss.builder.paratemters;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * 表单参数类
 *
 * @author JunJie
 */
public class FormParameter {

    private String    uri;
    private Object    object;
    private Component parent;
    private Map<String, Object> parameters = new HashMap<String, Object>();

    private FormParameter(String uri) {
        this.uri = uri;
    }

    /**
     * 返回指向的ZK页面的相对路径
     *
     * @return ZK页面的路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 返回ZK页面接收的表单对象
     *
     * @return 表单对象
     */
    public Object getObject() {
        return object;
    }

    /**
     * 返回ZK页面需要呈现的父组件页面
     *
     * @return 父组件对象
     */
    public Component getParent() {
        return parent;
    }

    /**
     * 返回ZK页面接收的参数
     *
     * @return 参数信息
     */
    public Map<String, Object> getParameters() {
        return unmodifiableMap(parameters);
    }

    /**
     * 参数构建类
     *
     * @author JunJie
     */
    public static class ParameterBuilder {

        private FormParameter parameter;

        /**
         * 构造方法。
         *
         * @param uri ZK页面的路径，不能为空
         */
        public ParameterBuilder(String uri) {
            Assert.notNull(uri, "Parameter URI cannot be null.");

            this.parameter = new FormParameter(uri);
        }

        /**
         * 设置ZK页面接收的表单对象
         *
         * @param object 表单对象
         * @return 参数构建类
         */
        public ParameterBuilder object(Object object) {
            this.parameter.object = object;
            return this;
        }

        /**
         * 设置ZK页面呈现的父页面
         *
         * @param parent 父组件
         * @return 参数构建类
         */
        public ParameterBuilder parent(Component parent) {
            this.parameter.parent = parent;
            return this;
        }

        /**
         * 设置ZK页面接收的参数
         *
         * @param k 参数的键
         * @param v 参数的值
         * @return 参数构建类
         */
        public ParameterBuilder parameter(String k, Object v) {
            this.parameter.parameters.put(k, v);
            return this;
        }

        /**
         * 设置ZK页面接收的参数集合
         *
         * @param params 参数集合对象
         * @return 参数构建类
         */
        public ParameterBuilder parameters(Map<String, Object> params) {
            if (MapUtils.isNotEmpty(params)) {
                this.parameter.parameters.putAll(params);
            }

            return this;
        }

        /**
         * 返回表单参数构建的实例对象
         *
         * @return <code>FormParameter</code>对象
         */
        public FormParameter build() {
            return parameter;
        }
    }
}
