package com.googlecode.easyec.spirit.web.webservice.support;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequest;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestFactory;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestNoResultHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.Assert;

/**
 * HTTP请求实现类。<br/>
 * 该类可帮助HTTP请求调用的处理类
 * 自动植入<code>EnvelopeFactory</code>
 * 对象实例（前提该对象实例已配置到Spring上下文中）
 *
 * @author JunJie
 * @see HttpRequestFactory
 */
public class AutowireCapableHttpRequestBean implements HttpRequest, BeanFactoryAware, FactoryBean<HttpRequest>, InitializingBean {

    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    private HttpRequest httpRequest;

    /**
     * 设置当前HTTP请求对象的实例。
     * 该实例负责实现最终的请求调用。
     *
     * @param httpRequest <code>HttpRequest</code>
     * @see HttpRequest
     */
    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.autowireCapableBeanFactory = (AutowireCapableBeanFactory) beanFactory;
    }

    public HttpRequest getObject() throws Exception {
        return this;
    }

    public Class<?> getObjectType() {
        return HttpRequest.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public <T> T request(HttpRequestHandler<T> handler) throws Exception {
        autowireBeanProperties(handler);

        return httpRequest.request(handler);
    }

    public void requestWithoutResult(HttpRequestNoResultHandler handler) throws Exception {
        autowireBeanProperties(handler);

        httpRequest.requestWithoutResult(handler);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(httpRequest, "HttpRequest cannot be null.");
    }

    /* 自动为请求处理类对象注入属性 */
    private void autowireBeanProperties(HttpRequestHandler<?> handler) {
        this.autowireCapableBeanFactory.autowireBeanProperties(
            handler, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true
        );
    }
}
