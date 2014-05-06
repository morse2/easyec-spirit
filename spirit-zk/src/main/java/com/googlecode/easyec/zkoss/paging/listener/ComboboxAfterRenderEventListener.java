package com.googlecode.easyec.zkoss.paging.listener;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;

/**
 * 下拉框数据行呈现后置事件处理类
 *
 * @author JunJie
 */
public abstract class ComboboxAfterRenderEventListener<T> implements SerializableEventListener<Event> {

    private static final long serialVersionUID = 427719520598756920L;
    private T source;

    public ComboboxAfterRenderEventListener(T source) {
        this.source = source;
    }

    public void onEvent(Event event) throws Exception {
        Combobox target = (Combobox) event.getTarget();
        ListModel<T> model = target.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (compare(model.getElementAt(i), source)) {
                target.setSelectedIndex(i);

                break;
            }
        }
    }

    /**
     * 比较下拉列表中和外部源数据是否相等
     *
     * @param destination 目标对象，来源于下拉框对象
     * @param source      源对象，来源于外部传递
     * @return 返回两个对象是否相等
     */
    abstract public boolean compare(T destination, T source);
}
