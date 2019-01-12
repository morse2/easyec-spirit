package com.googlecode.easyec.zkoss.utils;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 为组件操作提供通用的方法的工具类
 *
 * @author junjie
 */
public class ComponentUtils {

    private ComponentUtils() { }

    /**
     * 判断<code>InputEvent</code>事件
     * 传递的输入值是否有发生变化。如果发生
     * 变化，则返回true；否则返回false
     *
     * @param event 用户输入事件对象
     * @return 输入的值是否发生了变化
     */
    public static boolean isValueChanged(InputEvent event) {
        if (event != null) {
            String _prevVal = (String) event.getPreviousValue();
            String _currVal = event.getValue();
            return !StringUtils.equals(_prevVal, _currVal);
        }

        return false;
    }

    /**
     * 从<code>Combobox</code>组件中获取
     * 和给定值匹配的<code>Comboitem</code>
     * 选项的对象。该方法如果未能找到匹配值，
     * 则试图从当前所有选项中返回第一个选项值。
     *
     * @param cmb   ZK下拉框组件对象
     * @param value 用户输入的值
     * @return ZK选项组件对象
     */
    public static Comboitem getSelectedItem(Combobox cmb, String value) {
        return getSelectedItem(cmb, value, true);
    }

    /**
     * 从<code>Combobox</code>组件中获取
     * 和给定值匹配的<code>Comboitem</code>
     * 选项的对象。
     *
     * @param cmb      ZK下拉框组件对象
     * @param value    用户输入的值
     * @param setFirst 如果未能匹配到给定的值，是否要获取默认第一个选项值
     * @return ZK选项组件对象
     */
    public static Comboitem getSelectedItem(Combobox cmb, String value, boolean setFirst) {
        Comboitem _sel = cmb.getSelectedItem();
        if (_sel != null) return _sel;

        cmb.setValue(value);
        _sel = cmb.getSelectedItem();
        if (_sel != null) return _sel;

        List<Comboitem> items = cmb.getItems();
        if (setFirst && isNotEmpty(items)) {
            Comboitem _first = items.get(0);
            cmb.setSelectedItem(_first);

            return _first;
        }

        return null;
    }
}
