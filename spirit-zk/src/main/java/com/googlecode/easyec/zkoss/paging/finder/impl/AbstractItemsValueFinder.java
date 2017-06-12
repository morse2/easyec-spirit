package com.googlecode.easyec.zkoss.paging.finder.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Radiogroup;

/**
 * 抽象的多选项值选择的查找器对象类
 *
 * @author JunJie
 */
public abstract class AbstractItemsValueFinder<T extends Component> extends AbstractValueFinder<T> {

    public static final String REMAIN_AT_INDEX = "remainsAtIndex";
    private static final long serialVersionUID = -1322770777546989413L;

    protected void removeValues(Component comp) {
        Object remainsAtIndex = comp.getAttribute(REMAIN_AT_INDEX);
        if (remainsAtIndex != null) {
            int index = -1;
            if (remainsAtIndex instanceof String) {
                index = NumberUtils.toInt(((String) remainsAtIndex), -1);
            } else if (remainsAtIndex instanceof Number) {
                index = ((Number) remainsAtIndex).intValue();
            }

            if (index > -1) {
                if (comp instanceof Combobox) {
                    removeValues(((Combobox) comp), index);
                } else if (comp instanceof Radiogroup) {
                    removeValues((Radiogroup) comp, index);
                }
            }
        }
    }

    private void removeValues(Combobox comp, int index) {
        if (comp.getItemCount() > 0) {
            for (int i = (comp.getItemCount() - 1); i > index; i--) {
                comp.removeItemAt(i);
            }
        }
    }

    private void removeValues(Radiogroup comp, int index) {
        if (comp.getItemCount() > 0) {
            for (int i = (comp.getItemCount() - 1); i > index; i--) {
                comp.removeItemAt(i);
            }
        }
    }
}
