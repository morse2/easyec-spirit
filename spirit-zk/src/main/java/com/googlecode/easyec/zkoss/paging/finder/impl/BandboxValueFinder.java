package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Bandbox;

/**
 * ZK的Bandbox组件的查找值的对象类
 *
 * @author JunJie
 */
public class BandboxValueFinder extends AbstractValueFinder<Bandbox> {

    private static final long serialVersionUID = 7274577608901697444L;

    @Override
    protected Object getValue(Bandbox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Bandbox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                comp.setValue(((String) defaultValue));
            } else comp.setRawValue(defaultValue);
        }

        return getValue(comp);
    }
}
