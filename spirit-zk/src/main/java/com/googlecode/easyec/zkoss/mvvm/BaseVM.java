package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.utils.MessageboxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindComposer;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.web.util.resource.ServletRequestResolver;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.ComponentActivationListener;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.io.Serializable;
import java.util.Arrays;

import static org.zkoss.bind.annotation.ContextType.COMPONENT;

/**
 * 模型视图-视图模型的基础类。
 * 此类封装了 {@link Messagebox}的一些方法。
 *
 * @author JunJie
 */
public abstract class BaseVM<T extends Component> implements ComponentActivationListener, Serializable {

    private static final long serialVersionUID = 6175330378977715955L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /* 标识是否做过激活操作 */
    private transient boolean _activated;

    /* 当前ZK组件对象 */
    private T self;

    /**
     * 返回当前组件的引用对象
     *
     * @return <code>Component</code>子类
     */
    public T getSelf() {
        return self;
    }

    @Init
    public void initVM(@ContextParam(COMPONENT) T comp) {
        this.self = comp;
        // 注入全局变量
        wireVariables(comp);

        logger.trace("initVM() done!");
    }

    @AfterCompose
    public void afterInit() {
        // 注入组件对象
        Selectors.wireComponents(self, this, false);

        logger.trace("afterInit() done!");
    }

    /**
     * 为给定的ZK组件注入变量的方法
     *
     * @param comp ZK组件对象
     */
    protected void wireVariables(Component comp) {
        Selectors.wireVariables(comp, this,
            Arrays.<VariableResolver>asList(
                new DelegatingVariableResolver(),
                new ServletRequestResolver()
            )
        );
    }

    /**
     * 获取当前已设置的
     * <code>{@link Binder}</code>
     * 对象实例
     */
    protected Binder getBinder() {
        return (Binder) getSelf().getAttribute(
            (String) getSelf().getAttribute(
                BindComposer.BINDER_ID
            )
        );
    }

    public void didActivate(Component comp) {
        if (!_activated && self.equals(comp)) {
            wireVariables(comp);
            _activated = true;
        }
    }

    public void willPassivate(Component comp) {
        /* no op */
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
