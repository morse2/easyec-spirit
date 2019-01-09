package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.ui.Breadcrumb;
import com.googlecode.easyec.zkoss.ui.BreadcrumbCtrl;
import com.googlecode.easyec.zkoss.ui.DefaultBreadcrumbBuilder;
import com.googlecode.easyec.zkoss.ui.builders.DefaultUiParameterBuilder;
import com.googlecode.easyec.zkoss.ui.builders.UiBuilder;
import com.googlecode.easyec.zkoss.ui.events.BreadcrumbEvent;
import com.googlecode.easyec.zkoss.ui.listeners.StepOutEventListener;
import com.googlecode.easyec.zkoss.ui.listeners.StepOutUpperEventListener;
import com.googlecode.easyec.zkoss.ui.pushstate.DefaultPopState;
import com.googlecode.easyec.zkoss.ui.pushstate.PushState;
import com.googlecode.easyec.zkoss.viewmodel.ViewModelAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.web.util.resource.ServletRequestResolver;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.zkoss.bind.BindComposer.BINDER_ID;
import static org.zkoss.bind.annotation.ContextType.COMPONENT;
import static org.zkoss.bind.sys.BinderCtrl.VM;

/**
 * 视图-模型的基础类。
 * 该基类实现了VM的最一般的方法
 *
 * @author JunJie
 */
public abstract class BaseVM<T extends Component> implements ViewModelAware<T>, BreadcrumbCtrl {

    private static final long serialVersionUID = 7691905335357143308L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /* 标识是否做过激活操作 */
    private transient boolean _activated;
    private transient UiBuilder uiBuilder;

    /* 面包屑对象 */
    private Breadcrumb _bc;
    /* 当前ZK组件对象 */
    private T self;

    @WireVariable("uiBuilder")
    public void setUiBuilder(UiBuilder uiBuilder) {
        this.uiBuilder = uiBuilder;
    }

    @Override
    public UiBuilder getUiBuilder() {
        return this.uiBuilder;
    }

    @Override
    public T getSelf() {
        return this.self;
    }

    @Override
    public Breadcrumb getBreadcrumb() {
        return _bc;
    }

    @Override
    public Binder getBinder() {
        return (Binder) getSelf()
            .getAttribute((String)
                getSelf().getAttribute(BINDER_ID)
            );
    }

    @Init
    public void init(@ContextParam(COMPONENT) T comp) {
        this.self = comp;
        // 注入全局变量
        wireVariables(comp, false);

        logger.trace("initVM() done!");

        doInit();
    }

    @AfterCompose
    public void afterInit() {
        // 注入组件对象
        wireComponents(self, false);
        // 注入组件对象的事件
        wireEventListener(self);

        logger.trace("afterInit() done!");

        doAfterCompose();
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
        if (!_activated && Objects.equals(getSelf(), comp)) {
            wireVariables(comp, true);
            wireComponents(comp, true);
            _activated = true;
        }
    }

    public void willPassivate(Component comp) {
        _activated = false;
    }

    /**
     * 执行初始化操作的方法
     *
     * @see Init
     */
    protected void doInit() { /* no op */ }

    /**
     * 执行初始化后置操作的方法
     *
     * @see AfterCompose
     */
    protected void doAfterCompose() {
        this._bc = createBreadcrumb();
    }

    /**
     * 创建一组<code>VariableResolver</code>对象列表
     *
     * @return ZK变量解析对象列表
     */
    protected List<VariableResolver> createVariableResolvers() {
        return Stream.of(
            new com.googlecode.easyec.zkoss.DelegatingVariableResolver(),
            new DelegatingVariableResolver(),
            new ServletRequestResolver()
        ).collect(Collectors.toList());
    }

    /**
     * 从当前的ZK组件页面步入给定的UI页面参数构造
     * 器中的方法。如果给定的ZK页面组件创建成功，则
     * 当前页面将从当前的父页面组件中脱离出来，
     * 并加载新构造的页面组件。当新构造的页面触发
     * 事件onStepOut时，当前的页面组件才会
     * 被恢复并重新展现出来。
     *
     * @param builder URI参数构造器对象
     */
    public void stepIn(DefaultUiParameterBuilder builder) {
        Assert.notNull(builder, "UiParameterBuilder object mustn't be null.");

        // 强制设置父组件为null
        builder.setParent(null);

        Component _newComp = getUiBuilder().manufacture(builder.build());

        // 组件创建成功后
        if (_newComp != null) {
            StepOutEventListener lsnr = createStepOutEventListener();
            if (lsnr != null) {
                // 获取当前组件的父组件
                Component _parent = getSelf().getParent();
                // 托管当前页面
                getSelf().detach();
                // 移除当前页面的所有元素
                lsnr.removeChildren(_parent);
                // 设置新组件的父组件
                _newComp.setParent(_parent);
                // 添加onStepOut事件监听
                _newComp.addEventListener("onStepOut", lsnr);
                // 设置子组件的父级面包屑
                Object _vm = _newComp.getAttribute(VM);
                if (_vm instanceof BreadcrumbCtrl) {
                    Breadcrumb _bc = ((BreadcrumbCtrl) _vm).getBreadcrumb();
                    if (_bc != null) {
                        _bc.setParent(getBreadcrumb());
                        // 触发面包屑变更事件通知
                        Events.postEvent(new BreadcrumbEvent(_parent, _bc));
                    }
                }

                // 将当前组件状态推送至客户端
                PushState.push(new DefaultPopState(_newComp));
            }
        }
    }

    /**
     * 创建一个<code>StepOutEventListener</code>对象实例的方法
     *
     * @return {@link StepOutEventListener}
     */
    protected StepOutEventListener createStepOutEventListener() {
        return new StepOutUpperEventListener(getSelf());
    }

    /**
     * 创建一个面包屑控制对象实例
     *
     * @return {@link Breadcrumb}
     */
    protected Breadcrumb createBreadcrumb() {
        DefaultBreadcrumbBuilder builder = new DefaultBreadcrumbBuilder();
        builder.init(getSelf());

        return builder.getBreadcrumb();
    }
}
