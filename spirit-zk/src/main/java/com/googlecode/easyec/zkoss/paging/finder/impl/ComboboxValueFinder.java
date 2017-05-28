package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 * ZK的Combobox组件的查找值的对象类
 *
 * @author JunJie
 */
public class ComboboxValueFinder extends AbstractItemsValueFinder<Combobox> {

    private static final long serialVersionUID = 2365257185178054188L;

    @Override
    protected Object getValue(Combobox comp) {
        Comboitem si = comp.getSelectedItem();
        return si != null ? si.getValue() : null;
    }

    @Override
    protected Object resetValue(Combobox comp, Object defaultValue) {
        removeValues(comp);

        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                Comboitem item = _getSelectedItem(comp, ((String) defaultValue));
                if (item != null) {
                    comp.setSelectedItem(item);

                    return item.getValue();
                }
            }
        }
        // 如果没有默认值，那么取下拉框中的第一个元素
        else {
            if (comp.getItemCount() > 0) {
                Comboitem item = comp.getItemAtIndex(0);
                comp.setSelectedItem(item);

                return item.getValue();
            }
        }

        return null;
    }

    private Comboitem _getSelectedItem(Combobox box, String label) {
        for (int i = 0; i < box.getItemCount(); i++) {
            Comboitem item = box.getItemAtIndex(i);
            if (item != null && item.getLabel().equals(label)) {
                return item;
            }
        }

        return null;
    }
}
