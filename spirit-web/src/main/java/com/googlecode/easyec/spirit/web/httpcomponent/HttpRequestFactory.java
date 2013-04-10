package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.conn.ClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * HTTP请求工厂类。此类用于初始化一个默认的HTTP请求对象。
 *
 * @author JunJie
 */
public class HttpRequestFactory implements FactoryBean<HttpRequest>, InitializingBean {

    private HttpRequest httpRequest;
    private ClientConnectionManager clientConnectionManager;

    public void setClientConnectionManager(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
    }

    public HttpRequest getObject() throws Exception {
        return httpRequest;
    }

    public Class<?> getObjectType() {
        return HttpRequest.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(clientConnectionManager, "Parameter ClientConnectionManager is null.");

        httpRequest = new DefaultHttpRequest(clientConnectionManager);
    }
}
