package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Decimalbox;

import java.math.BigDecimal;

/**
 * ZK的Decimalbox的查找值的对象类
 *
 * @author JunJie
 */
public class DecimalboxValueFinder extends AbstractValueFinder<Decimalbox> {

    private static final long serialVersionUID = 6985629730772587547L;

    @Override
    protected Object getValue(Decimalbox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Decimalbox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof BigDecimal) {
                comp.setValue(((BigDecimal) defaultValue));
            } else if (defaultValue instanceof String) {
                comp.setValue((String) defaultValue);
            }
        } else comp.setValue(((BigDecimal) null));

        return getValue(comp);
    }
}
