package com.googlecode.easyec.zkoss.ui.builders;

import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.PageDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持构建UI组件的参数类
 *
 * @author junjie
 */
public abstract class UiParameter {

    private Component parent;
    private PageDefinition pageDefinition;
    private Map<Object, Object> args = new HashMap<>();

    protected UiParameter(PageDefinition pageDefinition) {
        Assert.notNull(pageDefinition, "PageDefinition is null.");
        this.pageDefinition = pageDefinition;
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
     * 返回ZK的<code>PageDefinition</code>对象
     *
     * @return ZK页面定义对象
     */
    public PageDefinition getPageDefinition() {
        return pageDefinition;
    }

    /**
     * 返回ZK页面接收的参数
     *
     * @return ZK页面的参数
     */
    public Map<?, ?> getArgs() {
        return args;
    }

    protected void setParent(Component parent) {
        this.parent = parent;
    }

    protected void setArgs(Map<Object, Object> args) {
        this.args = args;
    }

    /**
     * UI组件参数对象的默认实现类
     *
     * @author junjie
     */
    protected static class UiParameterImpl extends UiParameter {

        protected UiParameterImpl(PageDefinition pageDefinition) {
            super(pageDefinition);
        }
    }
}
