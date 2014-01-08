package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import com.googlecode.easyec.zkoss.zkjsp.databind.ReadonlyFieldTypeConverter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 对象属性值转换类。
 * <p>
 * 该类支持获取解析对象上的属性的值
 * </p>
 *
 * @author JunJie
 */
public class ObjectPropertyConverter extends ReadonlyFieldTypeConverter<Object, Object> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectPropertyConverter.class);

    public static final String PROPERTY_PATH = "property";
    public static final String REQUIRED_TYPE = "requiredType";

    @Override
    public Object coerceToUi(Object val, Map<String, Object> bindingArgs) {
        String property = (String) bindingArgs.get(PROPERTY_PATH);
        if (isBlank(property)) return val;

        try {
            Object o = PropertyUtils.getProperty(val, property);
            if (null != o) {
                String requiredType = (String) bindingArgs.get(REQUIRED_TYPE);
                return isNotBlank(requiredType)
                    ? ClassUtils.getClass(requiredType).cast(o)
                    : o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
