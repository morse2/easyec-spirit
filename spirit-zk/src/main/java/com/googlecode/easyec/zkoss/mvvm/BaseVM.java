package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.utils.MessageboxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import java.io.Serializable;

/**
 * 模型视图-视图模型的基础类。
 * 此类封装了 {@link Messagebox}的一些方法。
 *
 * @author JunJie
 */
public class BaseVM<T extends Component> implements Serializable {

    private static final long serialVersionUID = -453388816345015725L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private T self;

    /**
     * 返回当前组件的引用对象
     *
     * @return <code>Component</code>子类
     */
    public T getSelf() {
        return self;
    }

    @AfterCompose
    public void afterInit(@ContextParam(ContextType.COMPONENT) T comp) {
        this.self = comp;
        // 注入组件对象
        Selectors.wireComponents(comp, this, false);

        logger.trace("afterInit() done!");
    }

    /**
     * 显示一个提示信息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public void showInfo(String msg, String title) {
        MessageboxUtils.showInfo(msg, title);
    }

    /**
     * 显示一个带监听器的提示信息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public void showInfo(String msg, String title, SerializableEventListener<Event> listener) {
        MessageboxUtils.showInfo(msg, title, listener);
    }

    /**
     * 显示一个确认消息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public void showConfirm(String msg, String title) {
        MessageboxUtils.showConfirm(msg, title);
    }

    /**
     * 显示一个带消息监听器的确认消息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public void showConfirm(String msg, String title, SerializableEventListener<Event> listener) {
        MessageboxUtils.showConfirm(msg, title, listener);
    }

    /**
     * 显示一个警告框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public void showWarning(String msg, String title) {
        MessageboxUtils.showWarning(msg, title);
    }

    /**
     * 显示一个带监听器的警告框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public void showWarning(String msg, String title, SerializableEventListener<Event> listener) {
        MessageboxUtils.showWarning(msg, title, listener);
    }

    /**
     * 显示一个错误消息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public void showError(String msg, String title) {
        MessageboxUtils.showError(msg, title);
    }

    /**
     * 显示一个带监听器的错误消息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public void showError(String msg, String title, SerializableEventListener<Event> listener) {
        MessageboxUtils.showError(msg, title, listener);
    }
}
