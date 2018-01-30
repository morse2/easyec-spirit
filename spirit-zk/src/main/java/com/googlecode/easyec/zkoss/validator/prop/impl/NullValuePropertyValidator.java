package com.googlecode.easyec.zkoss.validator.prop.impl;

import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.prop.AbstractPropertyValidator;
import org.zkoss.bind.Property;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class NullValuePropertyValidator extends AbstractPropertyValidator {

    public NullValuePropertyValidator(String message) {
        super(message);
    }

    @Override
    public void validate(Property property) throws ValidationException {
        Object value = property.getValue();

        if (value == null) {
            throw new ValidationException(getMessage());
        }

        if (value instanceof String && isBlank(((String) value))) {
            throw new ValidationException(getMessage());
        }
    }
}
