package com.googlecode.easyec.zkoss.paging.finder;

import org.zkoss.zk.ui.Component;

import java.io.Serializable;

/**
 * 值查找对象类
 *
 * @author JunJie
 */
public interface ValueFinder<T extends Component> extends Serializable {

    /**
     * 指组件默认的值
     */
    String DEFAULT_VALUE_KEY = "defaultValue";

    /**
     * 获取给定ZK组件的值
     *
     * @param comp  ZK组件对象
     * @param reset 标记是否为重置状态
     * @return 值
     */
    Object getValue(T comp, boolean reset);
}
