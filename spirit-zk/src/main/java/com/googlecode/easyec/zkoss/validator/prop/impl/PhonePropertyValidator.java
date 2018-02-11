package com.googlecode.easyec.zkoss.validator.prop.impl;

import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.prop.AbstractPropertyValidator;
import org.zkoss.bind.Property;

import java.util.regex.Pattern;

public abstract class PhonePropertyValidator extends AbstractPropertyValidator {

    public PhonePropertyValidator(String message) {
        super(message);
    }

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
            throw new ValidationException(getMessage());
        }
    }

    /**
     * 获得电话格式的表达式
     *
     * @return <code>Pattern</code>
     */
    abstract protected Pattern getPhonePattern();
}
