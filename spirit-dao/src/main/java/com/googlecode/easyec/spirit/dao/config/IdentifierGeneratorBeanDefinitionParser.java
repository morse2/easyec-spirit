package com.googlecode.easyec.spirit.dao.config;

import com.googlecode.easyec.spirit.dao.id.support.PlatformSequenceGenerateDecisionInterceptor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

class IdentifierGeneratorBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return PlatformSequenceGenerateDecisionInterceptor.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        // inject attribute 'transactionManager'
        builder.addPropertyReference("transactionManager", element.getAttribute("transaction-manager"));
        // inject attribute 'order'
        builder.addPropertyValue("order", element.getAttribute("order"));

        // do parse child element named 'sequence-generator-chain'
        parseSequenceGenerator(
            DomUtils.getChildElementByTagName(element, "sequence-generator-chain"),
            parserContext,
            builder
        );
    }

    private void parseSequenceGenerator(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        BeanDefinitionBuilder beanDefinition = rootBeanDefinition(SequenceGeneratorChainFactoryBean.class);

        if (null != element) {
            // inject attribute 'data-source'
            beanDefinition.addPropertyReference("dataSource", element.getAttribute("data-source"));
            // inject attribute 'hilo-maxLoVal'
            beanDefinition.addPropertyValue("maxLoVal", element.getAttribute("hilo-maxLoVal"));
            // inject attribute 'converter'
            beanDefinition.addPropertyValue("converter", element.getAttribute("converter"));
            // inject attribute 'use-default'
            beanDefinition.addPropertyValue("useDefault", element.getAttribute("use-default"));

            // do parse child element 'custom-sequence-generator'
            Element childEle = DomUtils.getChildElementByTagName(element, "custom-identifier-generator");
            if (null != childEle) {
                List list = parserContext.getDelegate().parseListElement(
                    DomUtils.getChildElementByTagName(childEle, "list"),
                    builder.getRawBeanDefinition()
                );

                beanDefinition.addPropertyValue("customIdentifierGenerators", list);
            }
        }

        // inject property value 'sequenceGenerator'
        builder.addPropertyValue("sequenceGenerator", beanDefinition.getBeanDefinition());
    }
}
