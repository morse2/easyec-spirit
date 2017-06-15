package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 * ZK的Combobox组件的查找值的对象类
 *
 * @author JunJie
 */
public class ComboboxValueFinder extends AbstractValueFinder<Combobox> {

    public static final String REMAIN_AT_INDEX = "remainsAtIndex";
    private static final long serialVersionUID = -6978946441926362749L;

    @Override
    protected Object getValue(Combobox comp) {
        Comboitem si = comp.getSelectedItem();
        return si != null ? si.getValue() : null;
    }

    @Override
    protected Object resetValue(Combobox comp, Object defaultValue) {
        int index = -1;

        Object remainsAtIndex = comp.getAttribute(REMAIN_AT_INDEX);
        if (remainsAtIndex != null) {
            if (remainsAtIndex instanceof String) {
                index = NumberUtils.toInt(((String) remainsAtIndex), -1);
            } else if (remainsAtIndex instanceof Number) {
                index = ((Number) remainsAtIndex).intValue();
            }
        }

        if (index >= -1) removeValues(comp, index);

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

    private void removeValues(Combobox comp, int index) {
        if (comp.getItemCount() > 0) {
            for (int i = (comp.getItemCount() - 1); i > index; i--) {
                comp.removeItemAt(i);
            }
        }
    }
}
