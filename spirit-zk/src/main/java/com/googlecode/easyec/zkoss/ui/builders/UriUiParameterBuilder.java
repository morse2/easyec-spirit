package com.googlecode.easyec.zkoss.ui.builders;

import org.springframework.util.Assert;

import static org.zkoss.zk.ui.Executions.getCurrent;

public class UriUiParameterBuilder extends DefaultUiParameterBuilder {

    private String uri;

    protected UriUiParameterBuilder() { }

    public static UriUiParameterBuilder create() {
        return new UriUiParameterBuilder();
    }

    public UriUiParameterBuilder setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public UiParameter build() {
        Assert.notNull(uri, "Uri is null.");

        setPageDefinition(
            getCurrent().getPageDefinition(this.uri)
        );

        return super.build();
    }
}
