package com.googlecode.easyec.zkoss.validator.prop;

import com.googlecode.easyec.zkoss.validator.prop.PropertyValidator;
import org.springframework.util.Assert;

public abstract class AbstractPropertyValidator implements PropertyValidator {

    private String message;

    public AbstractPropertyValidator(String message) {
        Assert.notNull(message, "Parameter 'message' is null");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
