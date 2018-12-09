package com.googlecode.easyec.spirit.mybatis.config;

import com.googlecode.easyec.spirit.mybatis.mapper.config.DaoMapperScannerBeanDefinitionParser;
import com.googlecode.easyec.spirit.mybatis.paging.config.MybatisPageBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 分页配置类，用于处理命名空间的XML配置。
 *
 * @author JunJie
 */
public class SpiritMybatisConfigurerNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("paging", new MybatisPageBeanDefinitionParser());
        registerBeanDefinitionParser("scan", new DaoMapperScannerBeanDefinitionParser());
    }
}
