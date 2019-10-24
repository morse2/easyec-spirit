package com.googlecode.easyec.spirit.web.httpcomponent.ssl;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.SmartFactoryBean;

import javax.net.ssl.SSLContext;

public class TrustAllStrategyRegistryBean implements SmartFactoryBean<Registry<ConnectionSocketFactory>> {

    @Override
    public Registry<ConnectionSocketFactory> getObject() throws Exception {
        SSLContext sslContext
            = SSLContextBuilder.create()
            .loadTrustMaterial(TrustAllStrategy.INSTANCE)
            .build();

        return RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier()))
            .build();
    }

    @Override
    public Class<?> getObjectType() {
        return Registry.class;
    }
}
