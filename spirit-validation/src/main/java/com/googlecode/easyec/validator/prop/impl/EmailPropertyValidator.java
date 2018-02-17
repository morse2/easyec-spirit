package com.googlecode.easyec.validator.prop.impl;

import com.googlecode.easyec.validator.ValidationException;
import com.googlecode.easyec.validator.prop.AbstractMessagingPropertyValidator;
import com.googlecode.easyec.validator.prop.Property;

import java.util.regex.Pattern;

public class EmailPropertyValidator extends AbstractMessagingPropertyValidator {

    private static Pattern _pEmail
        = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
        Pattern.CASE_INSENSITIVE
    );

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

        if (!_pEmail.matcher((String) value).find()) {
            ValidationException ex = new ValidationException(getMessage());
            ex.setI18nMsg(isI18n());
            throw ex;
        }
    }
}
