package com.googlecode.easyec.zkoss.paging.listener;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;

/**
 * 选择事件的监听类
 *
 * @param <T> ZK组件对象
 * @param <E> 业务对象
 */
public interface SelectEventListener<T extends Component, E> extends SerializableEventListener<SelectEvent<T, E>> {
}
