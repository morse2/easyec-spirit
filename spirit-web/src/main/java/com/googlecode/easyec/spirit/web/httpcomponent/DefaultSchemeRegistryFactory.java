package com.googlecode.easyec.spirit.web.httpcomponent;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 默认的Scheme注册工厂类
 *
 * @author JunJie
 */
public class DefaultSchemeRegistryFactory implements FactoryBean<SchemeRegistry>, InitializingBean {

    private SchemeRegistry schemeRegistry;

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
        schemeRegistry = new SchemeRegistry();

        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
    }
}
