package com.googlecode.easyec.zkoss.ui.oper;

import org.zkoss.zk.ui.event.Event;

import java.io.Serializable;

/**
 * 按钮操作的接口类
 *
 * @author junjie
 */
public interface ButtonsOperation extends Serializable {

    /**
     * 当前点击Yes按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onYes(Event event) throws Exception;

    /**
     * 当前点击OK按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onOK(Event event) throws Exception;

    /**
     * 当前点击No按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onNo(Event event) throws Exception;

    /**
     * 当前点击Cancel按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onCancel(Event event) throws Exception;

    /**
     * 当前点击Abort按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onAbort(Event event) throws Exception;

    /**
     * 当前点击Ignore按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onIgnore(Event event) throws Exception;

    /**
     * 当前点击Retry按钮时候，
     * 触发此事件
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onRetry(Event event) throws Exception;

    /**
     * 当用户点击自定义按钮时候，
     * 触发此事件。换句话说，当
     * 按钮不属于<code>Messagebox</code>
     * 实例中的任何一个按钮时候，
     * 该方法将被触发调用。
     *
     * @param event 事件对象
     * @throws Exception 异常信息
     */
    void onOther(Event event) throws Exception;
}
