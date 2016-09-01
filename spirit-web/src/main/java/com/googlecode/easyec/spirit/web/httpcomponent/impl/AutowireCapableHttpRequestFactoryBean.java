package com.googlecode.easyec.spirit.web.httpcomponent.impl;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.util.Assert;

/**
 * HTTP请求实现类。<br/>
 * 该类可帮助HTTP请求调用的处理类
 * 自动植入<code>HttpRequestHandler</code>
 * 对象实例中的任何Spring bean
 * （前提该对象实例已配置到Spring上下文中）
 *
 * @author JunJie
 */
public class AutowireCapableHttpRequestFactoryBean implements SmartFactoryBean<HttpRequest>, InitializingBean {

    private HttpRequest httpRequest;

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
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
    public HttpRequest getObject() throws Exception {
        return new InternalAutowireCapableHttpRequestImpl(httpRequest);
    }

    @Override
    public Class<?> getObjectType() {
        return HttpRequest.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(httpRequest);
    }
}
