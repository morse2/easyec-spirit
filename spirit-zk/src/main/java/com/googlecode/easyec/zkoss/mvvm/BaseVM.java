package com.googlecode.easyec.zkoss.mvvm;

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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.ComponentActivationListener;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.zkoss.bind.annotation.ContextType.COMPONENT;

/**
 * 模型视图-视图模型的基础类。
 * 此类封装了 {@link Messagebox}的一些方法。
 *
 * @author JunJie
 */
public abstract class BaseVM<T extends Component> implements ComponentActivationListener, Serializable {

    private static final long serialVersionUID = 3833452443643290153L;
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
        wireVariables(comp, false);

        logger.trace("initVM() done!");
    }

    @AfterCompose
    public void afterInit() {
        // 注入组件对象
        wireComponents(self, false);
        // 注入组件对象的事件
        wireEventListener(self);

        logger.trace("afterInit() done!");
    }

    /**
     * 为给定的ZK组件注入变量的方法
     *
     * @param comp   ZK组件对象
     * @param rewire 表示是否执行重新注入的方法
     */
    protected void wireVariables(Component comp, boolean rewire) {
        if (rewire) {
            Selectors.rewireVariablesOnActivate(
                comp, this, createVariableResolvers()
            );
        } else {
            Selectors.wireVariables(
                comp, this, createVariableResolvers()
            );
        }
    }

    /**
     * 为给定的ZK组件注入相应组件的方法
     *
     * @param comp   ZK组件对象
     * @param rewire 表示是否执行重新注入的方法
     */
    protected void wireComponents(Component comp, boolean rewire) {
        if (rewire) {
            Selectors.rewireComponentsOnActivate(comp, this);
        } else {
            Selectors.wireComponents(comp, this, false);
        }
    }

    /**
     * 为给定的ZK组件注入相应组件事件的方法
     *
     * @param comp ZK组件对象
     */
    protected void wireEventListener(Component comp) {
        Selectors.wireEventListeners(comp, this);
    }

    public void didActivate(Component comp) {
        if (!_activated && self.equals(comp)) {
            wireVariables(comp, true);
            wireComponents(comp, true);
            _activated = true;
        }
    }

    public void willPassivate(Component comp) {
        _activated = false;
    }

    /**
     * 获取当前已设置的
     * <code>{@link Binder}</code>
     * 对象实例
     *
     * @return <code>Binder</code>对象的实现
     */
    protected Binder getBinder() {
        return (Binder) getSelf().getAttribute(
            (String) getSelf().getAttribute(
                BindComposer.BINDER_ID
            )
        );
    }

    /**
     * 创建一组<code>VariableResolver</code>对象列表
     *
     * @return ZK变量解析对象列表
     */
    protected List<VariableResolver> createVariableResolvers() {
        return Arrays.<VariableResolver>asList(
            new DelegatingVariableResolver(),
            new ServletRequestResolver()
        );
    }
}
