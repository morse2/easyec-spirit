package com.googlecode.easyec.validator.support;

public final class ValidationContextHolder {

    private static final ThreadLocal<Object> _t = new ThreadLocal<>();

    private ValidationContextHolder() { }

    public static Object get() {
        return _t.get();
    }

    public static void set(Object bean) {
        _t.set(bean);
    }

    public static void remove() {
        _t.remove();
    }
}
