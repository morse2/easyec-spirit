package com.googlecode.easyec.spirit.mybatis.cache.config;

import com.googlecode.easyec.spirit.mybatis.cache.interceptor.MybatisCacheAspectInterceptor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 默认注解方式缓存AOP切入初始化配置。
 *
 * @author JunJie
 */
class DefaultMybatisCacheAspectBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        // 设置order值
        builder.addPropertyValue("order", element.getAttribute("order"));
        // 如果指定了cacheService名称，则注入
        if (element.hasAttribute("cacheService-ref")) {
            builder.addPropertyReference("cacheService", element.getAttribute("cacheService-ref"));
        }
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MybatisCacheAspectInterceptor.class;
    }
}
