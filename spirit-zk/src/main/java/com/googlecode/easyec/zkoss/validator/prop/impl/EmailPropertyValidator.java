package com.googlecode.easyec.zkoss.validator.prop.impl;

import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.prop.AbstractPropertyValidator;
import org.zkoss.bind.Property;

import java.util.regex.Pattern;

public class EmailPropertyValidator extends AbstractPropertyValidator {

    private static Pattern _pEmail
        = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
        Pattern.CASE_INSENSITIVE
    );

    public EmailPropertyValidator(String message) {
        super(message);
    }

    @Override
    public void validate(Property property) throws ValidationException {
        Object value = property.getValue();
        if (value == null || !(value instanceof String)) {
            logger.warn(
                "The property [{}]'s value is not present. Value: [{}]. So ignore this validation.",
                property.getProperty(), value
            );

            return;
        }

        if (!_pEmail.matcher((String) value).find()) {
            throw new ValidationException(getMessage());
        }
    }
}
