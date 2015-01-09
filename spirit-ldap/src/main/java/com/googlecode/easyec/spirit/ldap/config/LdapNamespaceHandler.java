package com.googlecode.easyec.spirit.ldap.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * LDAP命名空间解析类
 *
 * @author JunJie
 */
public class LdapNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("transaction-advice", new TransactionAdviceParser());
    }
}
