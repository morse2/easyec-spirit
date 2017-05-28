package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Intbox;

/**
 * ZK的Intbox的查找值的对象类
 *
 * @author JunJie
 */
public class IntboxValueFinder extends AbstractValueFinder<Intbox> {

    private static final long serialVersionUID = 1952722110861609318L;

    @Override
    protected Object getValue(Intbox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Intbox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Integer) {
                comp.setValue((Integer) defaultValue);
            } else comp.setRawValue(defaultValue);
        }

        return getValue(comp);
    }
}
