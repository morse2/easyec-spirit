package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 对象属性值转国际化标签的类型转换类
 *
 * @author JunJie
 */
public class PropertyResourceLabelConverter extends ResourceLabelConverter {

    public static final String PROPERTY_PATH = "property";

    @Override
    public String coerceToUi(Object val, Map<String, Object> bindingArgs) {
        String property = (String) bindingArgs.get(PROPERTY_PATH);

        Assert.isTrue(isNotBlank(property), "Binding arg 'property' is required.");

        Object ret = null;

        try {
            ret = PropertyUtils.getProperty(val, property);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return super.coerceToUi(ret, bindingArgs);
    }
}
