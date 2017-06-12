package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Timebox;

import java.util.Date;

/**
 * ZK的Timebox的查找值的对象类
 *
 * @author JunJie
 */
public class TimeboxValueFinder extends AbstractValueFinder<Timebox> {

    private static final long serialVersionUID = 49843937381956912L;

    @Override
    protected Object getValue(Timebox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Timebox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Date) {
                comp.setValue(((Date) defaultValue));
            } else comp.setRawValue(defaultValue);
        } else comp.setValue(null);

        return getValue(comp);
    }
}
