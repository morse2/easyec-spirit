package com.googlecode.easyec.spirit.mybatis.mapper.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import static org.springframework.context.ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS;

public class DaoMapperScannerBeanDefinitionParser implements BeanDefinitionParser {

    private static final String ATTRIBUTE_BASE_PACKAGE = "base-package";
    private static final String ATTRIBUTE_FACTORY_REF = "factory-ref";

    @Override
    public synchronized BeanDefinition parse(Element element, ParserContext parserContext) {
        ClassPathDaoMapperScanner scanner = new ClassPathDaoMapperScanner(parserContext.getRegistry());
        XmlReaderContext readerContext = parserContext.getReaderContext();
        scanner.setResourceLoader(readerContext.getResourceLoader());
        String sqlSessionFactoryBeanName = element.getAttribute(ATTRIBUTE_FACTORY_REF);
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);

        scanner.registerFilters();
        String basePackage = element.getAttribute(ATTRIBUTE_BASE_PACKAGE);
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, CONFIG_LOCATION_DELIMITERS));

        return null;
    }
}
