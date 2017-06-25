package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import static org.apache.commons.lang.BooleanUtils.toBooleanObject;

/**
 * ZK的Combobox组件的查找值的对象类
 *
 * @author JunJie
 */
public class ComboboxValueFinder extends AbstractValueFinder<Combobox> {

    public static final String FIXED_COMBOITEM = "fixed";
    public static final String REMAIN_AT_INDEX = "remainsAtIndex";
    private static final long serialVersionUID = 9187626402333532730L;

    @Override
    protected Object getValue(Combobox comp) {
        Comboitem si = comp.getSelectedItem();
        return si != null ? si.getValue() : null;
    }

    @Override
    protected Object resetValue(Combobox comp, Object defaultValue) {
        Object argFixed = comp.getAttribute(FIXED_COMBOITEM);

        /*
         * 参数Fixed表示下拉框中的值是否是固定的，
         * 如果不是固定的，那么需要删除下拉框中的值
         */
        Boolean fixed = null;
        if (argFixed != null) {
            if (argFixed instanceof Boolean) {
                fixed = (Boolean) argFixed;
            } else fixed = toBooleanObject(argFixed.toString());
        }

        if (fixed != null && !fixed) {
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
        }

        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                Comboitem item = _getSelectedItem(comp, ((String) defaultValue));
                if (item != null) {
                    comp.setSelectedItem(item);

                    return item.getValue();
                }
            }
        } else {
            comp.setSelectedItem(null);
            comp.setValue(null);
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
