package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.ui.Steps;
import com.googlecode.easyec.zkoss.ui.builders.UriUiParameterBuilder;
import com.googlecode.easyec.zkoss.ui.listeners.StepsOutEventListener;
import com.googlecode.easyec.zkoss.ui.listeners.SwitchTabsEventListener;
import com.googlecode.easyec.zkoss.ui.listeners.UpdateTabsEventListener;
import com.googlecode.easyec.zkoss.ui.oper.UpdateTab;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.easyec.zkoss.mvvm.BaseFormVM.ARG_FORM_OBJECT;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.zkoss.bind.sys.BinderCtrl.VM;
import static org.zkoss.zk.ui.event.Events.ON_SELECT;

/**
 * 支持Tabbox组件标准化操作的视图模型的基类
 *
 * @author junjie
 */
@Init(superclass = true)
@AfterCompose(superclass = true)
public abstract class BaseTabsVM extends BaseVM<Tabbox> implements UpdateTab {

    public static final String ZUL_FILE = "zul-file";
    public static final String CAN_UPDATE = "can-update";
    public static final String WITH_FORM_OBJ = "with-form-obj";
    private static final long serialVersionUID = 2146695182065587202L;

    private final Map<Object, Object> args = new HashMap<>();
    private final ConcurrentMap<Tab, Component> _panelsRef = new ConcurrentHashMap<>();

    private Tabbox _tb;

    /**
     * 获取TB组件对象
     */
    public Tabbox getTabbox() {
        return this._tb;
    }

    @WireVariable("tabbox")
    public void setTabbox(Tabbox tabbox) {
        this._tb = tabbox;
    }

    protected Map<Object, Object> getArgs() {
        return this.args;
    }

    protected ConcurrentMap<Tab, Component> getPanelsRef() {
        return this._panelsRef;
    }

    @Override
    public void update(Tab tab) {
        // no op for default
    }

    @Override
    protected void doInit() {
        super.doInit();

        Map<?, ?> args = Executions.getCurrent().getArg();
        if (MapUtils.isNotEmpty(args)) {
            args.forEach(this.args::put);
        }
    }

    @Override
    protected void doAfterCompose() {
        Tabbox _tb = getTabbox();
        Assert.notNull(_tb.getTabs(), "ZK Tabs mustn't be null.");

        // 绑定onSelect事件
        SwitchTabsEventListener<?> stlsnr = createSwitchTabsEventListener();
        Assert.notNull(stlsnr, "SwitchTabsEventListener mustn't be null.");
        _tb.addEventListener(ON_SELECT, stlsnr);

        List<Component> _tabs = _tb.getTabs().getChildren();
        Assert.isTrue(isNotEmpty(_tabs), "No any ZK tab was present.");

        if (_tb.getTabpanels() == null) {
            new Tabpanels().setParent(_tb);
        }

        List<Component> _panels = _tb.getTabpanels().getChildren();
        if (size(_tabs) != size(_panels)) {
            for (int i = size(_panels); i < size(_tabs); i++) {
                new Tabpanel().setParent(_tb.getTabpanels());
            }
        }

        // 加载默认的页面
        Tab _selTb = _tb.getSelectedTab();
        if (_selTb != null) _loadPanel(_selTb);

        // 调用父类的doAfterCompose()方法
        super.doAfterCompose();
    }

    /* 卸载给定Panel中的内容 */
    private void _unloadPanel(Tab _t) {
        Component _comp = _panelsRef.get(_t);
        if (_comp != null) {
            _comp.setParent(null);
            _t.getLinkedPanel().invalidate();
        }
    }

    /* 加载给定Panel的内容 */
    private void _loadPanel(Tab _t) {
        Component _comp = this._panelsRef.get(_t);
        if (_comp != null) {
            Tabpanel _pnl = _t.getLinkedPanel();
            _comp.setParent(_pnl);

            // 强制重新计算高度
            _pnl.setHeight(null);

            return;
        }

        Tabpanel _selPanel = _t.getLinkedPanel();
        String zulFile = (String) _t.getAttribute(ZUL_FILE);

        final UriUiParameterBuilder _builder
            = getUriUiParameterBuilder()
            .setUri(zulFile);

        if (_isWithFormObj(_t)) {
            Object _form = this.args.get(ARG_FORM_OBJECT);
            if (_form != null) _builder.setArg(ARG_FORM_OBJECT, _form);
        }

        _comp = getUiBuilder().manufacture(
            _builder.setParent(_selPanel).build()
        );

        // 添加StepOut监听事件
        _comp.addEventListener(
            "onStepOut",
            new StepsOutEventListener(this, _selPanel)
        );

        // 添加Tabs组件更新的监听事件
        UpdateTabsEventListener utlsnr = createUpdateTabsEventListener();
        if (utlsnr != null) _comp.addEventListener("onUpdateTabs", utlsnr);

        // 添加引用关系
        this._panelsRef.putIfAbsent(_t, _comp);
    }

    private boolean _isWithFormObj(Component c) {
        Object _ret = c.getAttribute(WITH_FORM_OBJ);
        if (_ret == null) return true;
        if (_ret instanceof Boolean) return ((Boolean) _ret);
        if (_ret instanceof String) {
            Boolean _b = BooleanUtils.toBooleanObject(((String) _ret));
            return _b != null && _b;
        }

        return true;
    }

    @Command
    public void backOut() {
        // 获取当前选中的Tab组件
        Tab _selTb = getTabbox().getSelectedTab();
        if (_selTb == null) {
            stepOut();

            return;
        }

        // 获取当前正在呈现的组件对象
        Component _comp = _panelsRef.get(_selTb);
        if (_comp == null) {
            stepOut();

            return;
        }

        // 获取组件中绑定的VM
        Object _vm = _comp.getAttribute(VM);
        if (_vm == null) {
            stepOut();

            return;
        }

        // 卸载当前页面
        _unloadPanel(_selTb);

        // 判断VM是否为Steps的实例对象
        if (!(_vm instanceof Steps)) {
            stepOut();

            return;
        }

        // 调用子组件中的stepOut()方法
        ((Steps) _vm).stepOut();
    }

    /**
     * 获取配置的<code>UriUiParameterBuilder</code>对象实例
     *
     * @return <code>UriUiParameterBuilder</code>
     */
    abstract protected UriUiParameterBuilder getUriUiParameterBuilder();

    // ----- 创建事件的方法

    /**
     * 创建一个<code>Tab</code>切换时
     * 的监听类实例。该方法不能返回null
     *
     * @return <code>SwitchTabsEventListener</code>
     */
    protected SwitchTabsEventListener<?> createSwitchTabsEventListener() {
        return new DefaultSwitchTabsEventListener();
    }

    /**
     * 创建一个<code>Tab</code>更新时
     * 的监听类实例。该方法可以返回null，
     * 如果返回null，说明不需要监听该事件
     *
     * @return <code>UpdateTabsEventListener</code>
     */
    protected UpdateTabsEventListener createUpdateTabsEventListener() {
        return new DefaultUpdateTabsEventListener();
    }

    /**
     * Default <code>SwitchTabsEventListener</code>
     */
    private class DefaultSwitchTabsEventListener implements SwitchTabsEventListener<Object> {

        private static final long serialVersionUID = 5243516233247328300L;

        @Override
        public void onEvent(SelectEvent<Tab, Object> event) throws Exception {
            Set<Tab> unselectedTabs = event.getUnselectedItems();
            if (isNotEmpty(unselectedTabs)) {
                unselectedTabs.forEach(BaseTabsVM.this::_unloadPanel);
            }

            Set<Tab> selectedTabs = event.getSelectedItems();
            if (isNotEmpty(selectedTabs)) {
                selectedTabs.forEach(BaseTabsVM.this::_loadPanel);
            }
        }
    }

    /**
     * Default <code>UpdateTabsEventListener</code>
     */
    private class DefaultUpdateTabsEventListener implements UpdateTabsEventListener {

        private static final long serialVersionUID = 1606861951528297395L;

        @Override
        public void onEvent(Event event) throws Exception {
            Tabs tabs = BaseTabsVM.this.getTabbox().getTabs();
            List<Tab> children = tabs.getChildren();

            children.stream()
                .filter(tab -> tab.hasAttribute(CAN_UPDATE))
                .forEach(BaseTabsVM.this::update);
        }
    }
}
