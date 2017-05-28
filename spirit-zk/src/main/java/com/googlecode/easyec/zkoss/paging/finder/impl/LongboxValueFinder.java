package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Longbox;

/**
 * ZK的Longbox的查找值的对象类
 *
 * @author JunJie
 */
public class LongboxValueFinder extends AbstractValueFinder<Longbox> {

    private static final long serialVersionUID = -5880279848059777L;

    @Override
    protected Object getValue(Longbox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Longbox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Long) {
                comp.setValue(((Long) defaultValue));
            } else comp.setRawValue(defaultValue);
        } else comp.setValue(null);

        return getValue(comp);
    }
}
