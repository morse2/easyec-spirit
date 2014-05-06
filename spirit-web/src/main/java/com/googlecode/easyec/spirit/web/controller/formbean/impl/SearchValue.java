package com.googlecode.easyec.spirit.web.controller.formbean.impl;

/**
 * 搜索值对象
 *
 * @author JunJie
 */
final class SearchValue {

    private Object rawValue;
    private Object value;

    SearchValue(Object rawValue, Object value) {
        this.rawValue = rawValue;
        this.value = value;
    }

    public Object getRawValue() {
        return rawValue;
    }

    public Object getValue() {
        return value;
    }
}
