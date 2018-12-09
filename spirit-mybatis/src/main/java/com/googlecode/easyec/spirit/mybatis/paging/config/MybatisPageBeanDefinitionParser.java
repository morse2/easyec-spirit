package com.googlecode.easyec.spirit.mybatis.paging.config;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.dialect.impl.MySqlJdbcPageDialect;
import com.googlecode.easyec.spirit.dao.dialect.impl.OracleJdbcPageDialect;
import com.googlecode.easyec.spirit.dao.dialect.impl.PostgreSqlJdbcPageDialect;
import com.googlecode.easyec.spirit.mybatis.paging.support.MybatisPageProxy;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * Mybatis分页Bean定义的XML解析类。
 *
 * @author JunJie
 */
public class MybatisPageBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        // 定义分页的数量
        String pageSize = element.getAttribute("pageSize");
        Assert.isTrue(isNotBlank(pageSize), "Attribute 'pageSize' cannot be empty.");
        Assert.isTrue(isNumber(pageSize), "Attribute 'pageSize' is not a number.");

        Integer ps = Integer.valueOf(pageSize);
        Assert.isTrue(ps > 0, "Attribute 'pageSize' must be greater than zero.");

        // 定义分页方言类
        String dialect = element.getAttribute("dialect");
        Class<? extends PageDialect> dialectClass = parseDialectName(dialect);
        Assert.notNull(dialectClass, "Attribute 'dialect' cannot be empty.");

        BeanDefinitionBuilder dialectBean = rootBeanDefinition(dialectClass);

        // 定义分页代理类
        BeanDefinitionBuilder proxyBean = rootBeanDefinition(MybatisPageProxy.class);

        // 添加构造器引用参数
        builder.addConstructorArgValue(ps);
        builder.addConstructorArgValue(dialectBean.getBeanDefinition());
        builder.addConstructorArgValue(proxyBean.getBeanDefinition());
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return DefaultMybatisPageDelegate.class;
    }

    protected Class<? extends PageDialect> parseDialectName(String name) {
        if ("MySQL".equals(name)) return MySqlJdbcPageDialect.class;
        if ("Oracle".equals(name)) return OracleJdbcPageDialect.class;
        if ("PostgreSQL".equals(name)) return PostgreSqlJdbcPageDialect.class;

        return null;
    }
}
