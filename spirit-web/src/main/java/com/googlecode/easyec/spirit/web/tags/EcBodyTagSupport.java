package com.googlecode.easyec.spirit.web.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.tagext.BodyTagSupport;

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-13
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
public class EcBodyTagSupport extends BodyTagSupport {

    private static final long serialVersionUID = 946999972022429063L;

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
