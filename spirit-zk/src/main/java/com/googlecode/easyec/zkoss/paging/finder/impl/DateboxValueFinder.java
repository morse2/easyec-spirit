package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Datebox;

import java.util.Date;

/**
 * ZK的Datebox的查找值的对象类
 *
 * @author JunJie
 */
public class DateboxValueFinder extends AbstractValueFinder<Datebox> {

    private static final long serialVersionUID = 248126620864347920L;

    @Override
    protected Object getValue(Datebox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Datebox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof Date) {
                comp.setValue(((Date) defaultValue));
            } else comp.setRawValue(defaultValue);
        }

        return getValue(comp);
    }
}
