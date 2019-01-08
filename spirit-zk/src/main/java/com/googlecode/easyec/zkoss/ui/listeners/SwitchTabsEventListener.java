package com.googlecode.easyec.zkoss.ui.listeners;

import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.Tab;

/**
 * 切换<code>Tab</code>组件时，
 * 触发的事件监听类
 *
 * @author junjie
 */
public interface SwitchTabsEventListener<E> extends SerializableEventListener<SelectEvent<Tab, E>> {
}
