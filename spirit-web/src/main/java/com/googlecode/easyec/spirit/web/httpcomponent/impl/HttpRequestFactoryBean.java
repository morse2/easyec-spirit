package com.googlecode.easyec.spirit.web.httpcomponent.impl;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequest;
import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestFactory;
import org.apache.http.client.HttpClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * HTTP请求工厂类。此类用于初始化一个默认的HTTP请求对象。
 *
 * @author JunJie
 * @see HttpRequestFactory
 */
public class HttpRequestFactoryBean implements SmartFactoryBean<HttpRequest>, ApplicationContextAware {

    /**
     * 默认<code>HttpClient</code>对象Bean的名称
     */
    public static final String DEFAULT_HTTP_CLIENT_BEAN_NAME = "httpClient";

    private String httpClientBeanName = DEFAULT_HTTP_CLIENT_BEAN_NAME;
    private ApplicationContext applicationContext;

    /**
     * 设置<code>HttpClient</code>对象Bean的名称
     *
     * @param httpClientBeanName <code>HttpClient</code>对象引用的名字
     */
    public void setHttpClientBeanName(String httpClientBeanName) {
        this.httpClientBeanName = httpClientBeanName;
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
        return new InternalHttpRequestImpl(
            applicationContext.getBean(httpClientBeanName, HttpClient.class)
        );
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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
