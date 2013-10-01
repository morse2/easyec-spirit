package com.googlecode.easyec.zkoss.builder.paratemters;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * 组件构建参数类
 *
 * @author JunJie
 */
public class ComponentParameter {

    private String    uri;
    private Component parent;
    private Map<String, Object> parameters = new HashMap<String, Object>();

    private ComponentParameter(String uri) {
        this.uri = uri;
    }

    /**
     * 返回ZK组件指向的页面路径
     *
     * @return ZK页面路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 返回ZK组件的父组件
     *
     * @return 父组件对象
     */
    public Component getParent() {
        return parent;
    }

    /**
     * 返回ZK页面接收的参数
     *
     * @return ZK页面的参数
     */
    public Map<String, Object> getParameters() {
        return unmodifiableMap(parameters);
    }

    /**
     * ZK组件参数构建类
     */
    public static class ComponentParameterBuilder {

        private ComponentParameter parameter;

        /**
         * 构造方法
         *
         * @param uri ZK页面路径，不能为空
         */
        public ComponentParameterBuilder(String uri) {
            Assert.notNull(uri, "Parameter URI cannot be null.");

            this.parameter = new ComponentParameter(uri);
        }

        /**
         * 设置ZK页面呈现的父组件
         *
         * @param parent 父组件对象
         * @return ZK组件参数构建类
         */
        public ComponentParameterBuilder parent(Component parent) {
            this.parameter.parent = parent;
            return this;
        }

        /**
         * 设置ZK页面接收的参数
         *
         * @param k 参数的键
         * @param v 参数的值
         * @return ZK组件参数构建类
         */
        public ComponentParameterBuilder parameter(String k, Object v) {
            this.parameter.parameters.put(k, v);
            return this;
        }

        /**
         * 设置ZK页面接收的参数集合
         *
         * @param params 参数集合对象
         * @return ZK组件参数构建类
         */
        public ComponentParameterBuilder parameters(Map<String, Object> params) {
            if (MapUtils.isNotEmpty(params)) {
                this.parameter.parameters.putAll(params);
            }

            return this;
        }

        /**
         * 返回ZK参数对象实例
         *
         * @return <code>ComponentParameter</code>对象
         */
        public ComponentParameter build() {
            return this.parameter;
        }
    }
}
