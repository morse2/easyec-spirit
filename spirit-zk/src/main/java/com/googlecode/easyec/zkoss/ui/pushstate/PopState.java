package com.googlecode.easyec.zkoss.ui.pushstate;

import org.zkoss.zk.ui.Component;

import java.io.Serializable;

/**
 * 表示组件Push状态的接口类
 *
 * @author junjie
 */
public interface PopState extends Serializable {

    /**
     * 返回当前状态下的组件对象
     *
     * @return <code>Component</code>对象实例
     */
    Component getComponent();
}
