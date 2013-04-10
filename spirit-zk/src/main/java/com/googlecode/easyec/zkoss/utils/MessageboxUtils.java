package com.googlecode.easyec.zkoss.utils;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;

import static org.zkoss.zul.Messagebox.*;

/**
 * 消息弹出框工具类。
 *
 * @author JunJie
 */
public class MessageboxUtils {

    /**
     * 显示一个带监听器的消息框。
     *
     * @param msg      消息内容
     * @param title    标题
     * @param buttons  消息框的按钮
     * @param icon     消息框的图标
     * @param listener 消息框的监听器类对象
     */
    public static void showMessage(String msg, String title, int buttons, String icon, SerializableEventListener<Event> listener) {
        if (null == listener) listener = new NoOpMessageboxEventListener();

        show(msg, title, buttons, icon, listener);
    }

    /**
     * 显示一个提示信息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public static void showInfo(String msg, String title) {
        showInfo(msg, title, null);
    }

    /**
     * 显示一个带监听器的提示信息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public static void showInfo(String msg, String title, SerializableEventListener<Event> listener) {
        showMessage(msg, title, OK, INFORMATION, listener);
    }

    /**
     * 显示一个确认消息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public static void showConfirm(String msg, String title) {
        showConfirm(msg, title, null);
    }

    /**
     * 显示一个带消息监听器的确认消息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public static void showConfirm(String msg, String title, SerializableEventListener<Event> listener) {
        showMessage(msg, title, YES | NO, EXCLAMATION, listener);
    }

    /**
     * 显示一个警告框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public static void showWarning(String msg, String title) {
        showWarning(msg, title, null);
    }

    /**
     * 显示一个带监听器的警告框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public static void showWarning(String msg, String title, SerializableEventListener<Event> listener) {
        showMessage(msg, title, OK, EXCLAMATION, listener);
    }

    /**
     * 显示一个错误消息框。
     *
     * @param msg   消息内容
     * @param title 消息标题
     */
    public static void showError(String msg, String title) {
        showError(msg, title, null);
    }

    /**
     * 显示一个带监听器的错误消息框。
     *
     * @param msg      消息内容
     * @param title    消息标题
     * @param listener 消息监听器类对象
     */
    public static void showError(String msg, String title, SerializableEventListener<Event> listener) {
        showMessage(msg, title, OK, ERROR, listener);
    }

    /**
     * 无事件操作的消息框的监听器类。
     */
    private static class NoOpMessageboxEventListener implements SerializableEventListener<Event> {

        private static final long serialVersionUID = 1391496987168251090L;

        public void onEvent(Event event) throws Exception {
            // no op
        }
    }
}
