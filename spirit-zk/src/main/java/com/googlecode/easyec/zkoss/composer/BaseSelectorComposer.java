package com.googlecode.easyec.zkoss.composer;

import com.googlecode.easyec.zkoss.utils.MessageboxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Messagebox;

/**
 * 选择器的控制器基础类。
 * 此类封装了 {@link Messagebox}的一些方法。
 *
 * @author JunJie
 */
public class BaseSelectorComposer<T extends Component> extends SelectorComposer<T> {

    private static final long serialVersionUID = -7152248125401930368L;
    /**
     * SLF4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
