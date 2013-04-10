package com.googlecode.easyec.spirit.web.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.tagext.TagSupport;

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;

/**
 * 支持Spring上下文的JSTL标签类。
 *
 * @author JunJie
 */
public class EcTagSupport extends TagSupport {

    private static final long serialVersionUID = -8054668024977165446L;

    /**
     * SLF4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 从Spring上下文中查找一个定义的Bean信息。
     *
     * @param clazz Bean类型
     * @param <T>   泛型
     * @return Bean类型的实例
     */
    public <T> T getBean(Class<T> clazz) {
        return getRequiredWebApplicationContext(pageContext.getServletContext()).getBean(clazz);
    }
}
