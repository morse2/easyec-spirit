package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

/**
 * ZK的Radiogroup的查找值的对象类
 *
 * @author JunJie
 */
public class RadiogroupValueFinder extends AbstractValueFinder<Radiogroup> {

    private static final long serialVersionUID = -7043529848256965330L;

    @Override
    protected Object getValue(Radiogroup comp) {
        Radio si = comp.getSelectedItem();
        return si != null ? si.getValue() : null;
    }

    @Override
    protected Object resetValue(Radiogroup comp, Object defaultValue) {
        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                Radio item = _getSelectedItem(comp, ((String) defaultValue));
                if (item != null) {
                    comp.setSelectedItem(item);

                    return item.getValue();
                }
            }
        }
        // 如果没有默认值，那么聚焦到第一个Radio组件上
        else {
            if (comp.getItemCount() > 0) {
                Radio item = comp.getItemAtIndex(0);
                comp.setSelectedItem(item);

                return item.getValue();
            }
        }

        return null;
    }

    private Radio _getSelectedItem(Radiogroup group, String label) {
        for (int i = 0; i < group.getItemCount(); i++) {
            Radio item = group.getItemAtIndex(i);
            if (item != null && item.getLabel().equals(label)) {
                return item;
            }
        }

        return null;
    }
}
