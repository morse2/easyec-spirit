package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Checkbox;

/**
 * ZK的Checkbox的查找值的对象类
 *
 * @author JunJie
 */
public class CheckboxValueFinder extends AbstractValueFinder<Checkbox> {

    private static final long serialVersionUID = -1036540480812568224L;

    @Override
    protected Object getValue(Checkbox comp) {
        return comp.isChecked() ? comp.getValue() : null;
    }

    @Override
    protected Object resetValue(Checkbox comp, Object defaultValue) {
        if (defaultValue != null) {
            comp.setChecked(
                defaultValue.equals(comp.getValue())
            );
        } else comp.setChecked(false);

        return getValue(comp);
    }
}
