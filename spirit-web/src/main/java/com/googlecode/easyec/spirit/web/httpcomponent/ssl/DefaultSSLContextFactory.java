package com.googlecode.easyec.spirit.web.httpcomponent.ssl;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;

/**
 * 默认的SSL上下文初始化工厂类
 *
 * @author JunJie
 */
public class DefaultSSLContextFactory implements FactoryBean<SSLContext>, InitializingBean {

    private SSLContext     sslContext;
    private KeyManager[]   keyManager;
    private TrustManager[] trustManager;
    private String         instanceName;

    /**
     * 设置当前SSL上下文的实例名
     *
     * @param instanceName 实例名，例如：TLS，SSL，SSLv2
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * 设置一组密钥管理器类
     *
     * @param keyManager 密钥管理器
     */
    public void setKeyManager(KeyManager[] keyManager) {
        this.keyManager = keyManager;
    }

    /**
     * 设置一组信任管理器类
     *
     * @param trustManager 信任管理器
     */
    public void setTrustManager(TrustManager[] trustManager) {
        this.trustManager = trustManager;
    }

    public SSLContext getObject() throws Exception {
        return sslContext;
    }

    public Class<?> getObjectType() {
        return SSLContext.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(instanceName, "SSL Context instance name is null.");

        sslContext = SSLContext.getInstance(instanceName);
        sslContext.init(keyManager, trustManager, new SecureRandom());
    }
}
