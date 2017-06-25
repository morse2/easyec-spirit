package com.googlecode.easyec.zkoss.paging.finder.impl;

import com.googlecode.easyec.zkoss.paging.finder.ValueFinder;
import org.zkoss.zk.ui.Component;

/**
 * 抽象的支持ZK组件值查找的对象类
 *
 * @author JunJie
 */
public abstract class AbstractValueFinder<T extends Component> implements ValueFinder<T> {

    private static final long serialVersionUID = 1721575977904900024L;

    public Object getValue(T comp, boolean reset) {
        if (comp == null) return null;

        return !reset
            ? getValue(comp)
            : resetValue(comp, comp.getAttribute(DEFAULT_VALUE_KEY));
    }

    /**
     * 获取给定组件的当前值
     *
     * @param comp ZK组件对象
     * @return 组件值
     */
    abstract protected Object getValue(T comp);

    /**
     * 重置给定组件的值（初始化组件值）
     *
     * @param comp         ZK组件对象
     * @param defaultValue 组件的默认值（由用户设定）
     * @return 重置操作完成后的组件值
     */
    abstract protected Object resetValue(T comp, Object defaultValue);
}
