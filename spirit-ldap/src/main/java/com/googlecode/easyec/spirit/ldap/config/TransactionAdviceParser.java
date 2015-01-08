package com.googlecode.easyec.spirit.ldap.config;

import com.googlecode.easyec.spirit.ldap.transaction.support.ContextSourceTransactionAdviser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.ldap.transaction.compensating.support.DefaultTempEntryRenamingStrategy;
import org.springframework.ldap.transaction.compensating.support.DifferentSubtreeTempEntryRenamingStrategy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.ldap.config.Elements.DEFAULT_RENAMING_STRATEGY;
import static org.springframework.ldap.config.Elements.DIFFERENT_SUBTREE_RENAMING_STRATEGY;
import static org.springframework.ldap.transaction.compensating.support.DefaultTempEntryRenamingStrategy.DEFAULT_TEMP_SUFFIX;

/**
 * LDAP事务监视解析类
 *
 * @author JunJie
 */
final class TransactionAdviceParser implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String contextSourceRef = _getString(element, "context-source-ref", "contextSource");

        BeanDefinitionBuilder builder = rootBeanDefinition(ContextSourceTransactionAdviser.class);

        // register property order
        String order = _getString(element, "order", "1");
        builder.addPropertyValue("order", Integer.parseInt(order));

        // register contextSource reference
        builder.addPropertyReference("contextSource", contextSourceRef);

        // register renaming strategy
        Element defaultStrategyChild = DomUtils.getChildElementByTagName(element, DEFAULT_RENAMING_STRATEGY);
        Element differentSubtreeChild = DomUtils.getChildElementByTagName(element, DIFFERENT_SUBTREE_RENAMING_STRATEGY);

        if (defaultStrategyChild != null) {
            builder.addPropertyValue("renamingStrategy", _parseDefaultRenamingStrategy(defaultStrategyChild));
        }

        if (differentSubtreeChild != null) {
            builder.addPropertyValue("renamingStrategy", _parseDifferentSubtreeRenamingStrategy(differentSubtreeChild));
        }

        String id = _getString(element, "id", "contextSourceTransactionAware");
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinition, id));

        return beanDefinition;
    }

    private String _getString(Element element, String attribute, String defaultValue) {
        String theValue = element.getAttribute(attribute);
        if (StringUtils.hasText(theValue)) {
            return theValue;
        }

        return defaultValue;
    }

    private BeanDefinition _parseDifferentSubtreeRenamingStrategy(Element element) {
        BeanDefinitionBuilder builder = rootBeanDefinition(DifferentSubtreeTempEntryRenamingStrategy.class);

        String subtreeNode = element.getAttribute("subtree-node");
        Assert.hasText(subtreeNode, "'subtree-node' must be specified");

        builder.addConstructorArgValue(subtreeNode);

        return builder.getBeanDefinition();
    }

    private BeanDefinition _parseDefaultRenamingStrategy(Element element) {
        BeanDefinitionBuilder builder = rootBeanDefinition(DefaultTempEntryRenamingStrategy.class);
        builder.addPropertyValue("tempSuffix", _getString(element, "temp-suffix", DEFAULT_TEMP_SUFFIX));

        return builder.getBeanDefinition();
    }
}
