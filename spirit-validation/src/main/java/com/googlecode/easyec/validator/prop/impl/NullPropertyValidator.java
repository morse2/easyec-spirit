package com.googlecode.easyec.validator.prop.impl;

import com.googlecode.easyec.validator.ValidationException;
import com.googlecode.easyec.validator.prop.AbstractMessagingPropertyValidator;
import com.googlecode.easyec.validator.prop.Property;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class NullPropertyValidator extends AbstractMessagingPropertyValidator {

    @Override
    public void validate(Property property) throws ValidationException {
        Object value = getPropertyValue(property);

        if (value == null) {
            logger.warn("Property [{}]'s value is null.", property.getProperty());

            ValidationException ex = new ValidationException(getMessage());
            ex.setLocalized(isLocalized());
            throw ex;
        }

        if (value instanceof String && isBlank(((String) value))) {
            logger.warn(
                "Property [{}] is instanceof java.lang.String, but has no value.",
                property.getProperty()
            );

            ValidationException ex = new ValidationException(getMessage());
            ex.setLocalized(isLocalized());
            throw ex;
        }
    }
}
