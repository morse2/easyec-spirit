package com.googlecode.easyec.zkoss.validator.prop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public abstract class AbstractPropertyValidator implements PropertyValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String message;

    public AbstractPropertyValidator(String message) {
        Assert.notNull(message, "Parameter 'message' is null");
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
