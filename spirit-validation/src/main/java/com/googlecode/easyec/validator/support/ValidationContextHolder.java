package com.googlecode.easyec.validator.support;

import org.springframework.beans.PropertyAccessor;

public final class ValidationContextHolder {

    private static final ThreadLocal<PropertyAccessor> _t = new ThreadLocal<>();

    private ValidationContextHolder() { }

    public static PropertyAccessor get() {
        return _t.get();
    }

    public static void set(PropertyAccessor bean) {
        _t.set(bean);
    }

    public static void remove() {
        _t.remove();
    }
}
