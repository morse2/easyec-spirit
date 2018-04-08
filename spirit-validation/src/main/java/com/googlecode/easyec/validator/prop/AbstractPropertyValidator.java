package com.googlecode.easyec.validator.prop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.zkoss.bind.proxy.FormProxyObject;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class AbstractPropertyValidator implements PropertyValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Object getPropertyValue(Property property) {
        Object value = property.getValue();
        if (value != null && !_isNullString(value)) return value;

        String propertyName = property.getProperty();
        // 如果当前页面上没有传递过来值，那么就从当前表单对象中查找
        BeanWrapper bw = new BeanWrapperImpl(property.getBase());
        if (!bw.isReadableProperty(propertyName)) {
            logger.warn("No readable property [{}] is present.", propertyName);

            return null;
        }

        value = bw.getPropertyValue(propertyName);

        return _isNullString(value) ? null : value;
    }

    protected Object getOrigin(Property property) {
        if (null == property) return null;

        Object base = property.getBase();
        if (base instanceof FormProxyObject) {
            return ((FormProxyObject) base).getOriginObject();
        }

        return null;
    }

    private boolean _isNullString(Object val) {
        return val instanceof String && isBlank((CharSequence) val);
    }
}
