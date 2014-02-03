package com.googlecode.easyec.spirit.dao.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * DAO工具类命名空间配置处理类
 *
 * @author JunJie
 */
public class DaoNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("identifier-generator", new IdentifierGeneratorBeanDefinitionParser());
    }
}
