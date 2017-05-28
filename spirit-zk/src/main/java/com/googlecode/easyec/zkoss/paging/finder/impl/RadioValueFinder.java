package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Radio;

/**
 * ZK的Radio的查找值的对象类
 *
 * @author JunJie
 */
public class RadioValueFinder extends AbstractValueFinder<Radio> {

    private static final long serialVersionUID = 6850634020641529203L;

    @Override
    protected Object getValue(Radio comp) {
        return comp.isSelected() ? comp.getValue() : null;
    }

    @Override
    protected Object resetValue(Radio comp, Object defaultValue) {
        if (defaultValue != null) {
            comp.setSelected(
                defaultValue.equals(comp.getValue())
            );
        } else comp.setSelected(false);

        return null;
    }
}
