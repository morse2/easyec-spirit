package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Spinner;

/**
 * ZK的Spinner的查找值的对象类
 */
public class SpinnerValueFinder extends AbstractValueFinder<Spinner> {

    private static final long serialVersionUID = 620500786702504865L;

    @Override
    protected Object getValue(Spinner comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Spinner comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Integer) {
                comp.setValue(((Integer) defaultValue));
            } else comp.setRawValue(defaultValue);
        } else comp.setValue(null);

        return getValue(comp);
    }
}
