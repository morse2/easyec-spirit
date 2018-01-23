package com.googlecode.easyec.zkoss.ui.builders;

import com.googlecode.easyec.zkoss.ui.builders.UiParameter.UiParameterImpl;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.PageDefinition;

import java.util.HashMap;
import java.util.Map;

public class DefaultUiParameterBuilder {

    private Component parent;
    private PageDefinition pageDefinition;
    private Map<Object, Object> args = new HashMap<>();

    protected DefaultUiParameterBuilder() { }

    public static DefaultUiParameterBuilder create() {
        return new DefaultUiParameterBuilder();
    }

    public DefaultUiParameterBuilder setParent(Component parent) {
        this.parent = parent;
        return this;
    }

    public DefaultUiParameterBuilder setPageDefinition(PageDefinition pageDefinition) {
        this.pageDefinition = pageDefinition;
        return this;
    }

    public DefaultUiParameterBuilder setArg(Object k, Object v) {
        this.args.put(k, v);
        return this;
    }

    public DefaultUiParameterBuilder setArgs(Map<Object, Object> args) {
        if (MapUtils.isNotEmpty(args)) {
            this.args.putAll(args);
        }

        return this;
    }

    public UiParameter build() {
        Assert.notNull(this.pageDefinition, "PageDefinition is null.");
        Assert.notNull(this.parent, "Parent component is null.");

        UiParameterImpl impl = new UiParameterImpl(pageDefinition);
        impl.setParent(this.parent);
        impl.setArgs(this.args);

        return impl;
    }
}
