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

        try {
            Assert.isTrue(isNotBlank(property), "Binding arg 'property' is required.");

            return super.coerceToUi(
                PropertyUtils.getProperty(val, property),
                bindingArgs
            );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
