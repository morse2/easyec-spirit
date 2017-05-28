package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Textbox;

/**
 * 支持ZK组件<code>Textbox</code>值查找的对象类
 *
 * @author JunJie
 */
public class TextboxValueFinder extends AbstractValueFinder<Textbox> {

    private static final long serialVersionUID = -2904044271095950448L;

    @Override
    protected Object getValue(Textbox comp) {
        return comp.getValue();
    }

    @Override
    protected Object resetValue(Textbox comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                comp.setValue(((String) defaultValue));
            } else comp.setRawValue(defaultValue);
        } else comp.setValue(null);

        return comp.getValue();
    }
}
