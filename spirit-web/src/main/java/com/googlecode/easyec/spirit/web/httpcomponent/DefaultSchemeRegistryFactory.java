package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 默认的Scheme注册工厂类
 *
 * @author JunJie
 */
public class DefaultSchemeRegistryFactory implements FactoryBean<SchemeRegistry>, InitializingBean {

    private PlainSocketFactory plainSocketFactory = PlainSocketFactory.getSocketFactory();
    private SSLSocketFactory   sslSocketFactory   = SSLSocketFactory.getSocketFactory();
    private SchemeRegistry schemeRegistry;

    /**
     * 设置SSL的套接字工厂类实现
     *
     * @param sslSocketFactory SSL套接字工厂类
     */
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    /**
     * 设置明文的套接字工厂类实现
     *
     * @param plainSocketFactory 明文套接字工厂类
     */
    public void setPlainSocketFactory(PlainSocketFactory plainSocketFactory) {
        this.plainSocketFactory = plainSocketFactory;
    }

    public SchemeRegistry getObject() throws Exception {
        return schemeRegistry;
    }

    public Class<?> getObjectType() {
        return SchemeRegistry.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(plainSocketFactory, "Plain socket factory object is null.");
        Assert.notNull(sslSocketFactory, "SSL socket factory object is null.");

        schemeRegistry = new SchemeRegistry();

        schemeRegistry.register(new Scheme("http", 80, plainSocketFactory));
        schemeRegistry.register(new Scheme("https", 443, sslSocketFactory));
    }
}
