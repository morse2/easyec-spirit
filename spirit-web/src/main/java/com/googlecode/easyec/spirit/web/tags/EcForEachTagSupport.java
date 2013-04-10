package com.googlecode.easyec.spirit.web.tags;

import org.apache.taglibs.standard.tag.common.core.ForEachSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-7-22
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
public class EcForEachTagSupport extends ForEachSupport {

    private static final long serialVersionUID = 6690227574431752372L;

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
