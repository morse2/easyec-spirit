package com.googlecode.easyec.validator.prop.impl;

import com.googlecode.easyec.validator.ValidationException;
import com.googlecode.easyec.validator.prop.AbstractMessagingPropertyValidator;
import com.googlecode.easyec.validator.prop.Property;

import java.util.regex.Pattern;

public abstract class PhonePropertyValidator extends AbstractMessagingPropertyValidator {

    @Override
    public void validate(Property property) throws ValidationException {
        Object value = getPropertyValue(property);

        if (value == null || !(value instanceof String)) {
            logger.warn(
                "The property [{}]'s value is not present. Value: [{}]. So ignore this validation.",
                property.getProperty(), null
            );

            return;
        }

        if (!getPhonePattern().matcher((String) value).find()) {
            ValidationException ex = new ValidationException(getMessage());
            ex.setI18nMsg(isI18n());
            throw ex;
        }
    }

    /**
     * 获得电话格式的表达式
     *
     * @return <code>Pattern</code>
     */
    abstract protected Pattern getPhonePattern();
}
