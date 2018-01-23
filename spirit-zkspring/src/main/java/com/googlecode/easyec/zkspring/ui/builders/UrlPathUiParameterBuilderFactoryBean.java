package com.googlecode.easyec.zkspring.ui.builders;

import com.googlecode.easyec.zkoss.ui.builders.PreSufPathUriUiParameterBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.util.Assert;

/**
 * @author junjie
 */
public class UrlPathUiParameterBuilderFactoryBean implements SmartFactoryBean<PreSufPathUriUiParameterBuilder>, InitializingBean {

    private String prefix;
    private String suffix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean isPrototype() {
        return true;
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }

    @Override
    public PreSufPathUriUiParameterBuilder getObject() throws Exception {
        return PreSufPathUriUiParameterBuilder
            .create()
            .setPrefix(prefix)
            .setSuffix(suffix);
    }

    @Override
    public Class<?> getObjectType() {
        return PreSufPathUriUiParameterBuilder.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(prefix, "Parameter prefix cannot be null.");
        Assert.notNull(suffix, "Parameter suffix cannot be null.");

        if (!prefix.endsWith("/")) prefix = prefix + "/";
        if (!suffix.startsWith(".")) suffix = "." + suffix;
    }
}
