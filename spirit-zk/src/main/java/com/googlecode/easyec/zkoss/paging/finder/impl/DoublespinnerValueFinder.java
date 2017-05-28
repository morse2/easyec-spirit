package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Doublespinner;

/**
 * ZK的Doublespinner的查找值的对象类
 *
 * @author JunJie
 */
public class DoublespinnerValueFinder extends AbstractValueFinder<Doublespinner> {

    private static final long serialVersionUID = 4988898615461545478L;

    @Override
    protected Object getValue(Doublespinner comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Doublespinner comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Double) {
                comp.setValue(((Double) defaultValue));
            } else comp.setRawValue(defaultValue);
        }

        return getValue(comp);
    }
}
