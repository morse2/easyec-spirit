package com.googlecode.easyec.validator.support;

import org.springframework.beans.PropertyAccessor;
import org.springframework.util.Assert;

public final class ValidationObject {

    private PropertyAccessor accessor;
    private String command;

    ValidationObject(PropertyAccessor accessor, String command) {
        Assert.notNull(accessor, "PropertyAccessor mustn't be null.");

        this.accessor = accessor;
        this.command = command;
    }

    public PropertyAccessor getAccessor() {
        return accessor;
    }

    public String getCommand() {
        return command;
    }
}
