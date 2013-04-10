package com.googlecode.easyec.spirit.mybatis.cache.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Mybatis缓存切入XML文件命名空间配置处理类。
 *
 * @author JunJie
 */
public class MybatisCacheAspectNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("aspectj-config", new DefaultMybatisCacheAspectBeanDefinitionParser());
    }
}
