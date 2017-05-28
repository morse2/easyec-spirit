package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Doublebox;

/**
 * ZK的Doublebox的查找值的对象类
 *
 * @author JunJie
 */
public class DoubleboxValueFinder extends AbstractValueFinder<Doublebox> {

    private static final long serialVersionUID = 928726192234207388L;

    @Override
    protected Object getValue(Doublebox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Doublebox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Double) {
                comp.setValue((Double) defaultValue);
            } else comp.setRawValue(defaultValue);
        }

        return getValue(comp);
    }
}
