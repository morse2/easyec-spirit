package com.googlecode.easyec.spirit.mybatis.paging.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 分页配置类，用于处理命名空间的XML配置。
 *
 * @author JunJie
 */
public class PageConfigurerNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("mybatis-config", new MybatisPageBeanDefinitionParser());
    }
}
