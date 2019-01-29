package com.googlecode.easyec.validator.support;

import static org.springframework.beans.PropertyAccessorFactory.forBeanPropertyAccess;

public final class ValidationContextHolder {

    private static final ThreadLocal<ValidationObject> _t = new ThreadLocal<>();

    private ValidationContextHolder() { }

    public static ValidationObject get() {
        return _t.get();
    }

    public static void set(Object bean, String command) {
        _t.set(
            new ValidationObject(
                forBeanPropertyAccess(bean), command
            )
        );
    }

    public static void remove() {
        _t.remove();
    }
}
