package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.ui.builders.PreSufPathUriUiParameterBuilder;
import com.googlecode.easyec.zkoss.ui.listeners.StepsOutEventListener;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.zkoss.zk.ui.event.Events.ON_SELECT;

/**
 * 支持Tabbox组件标准化操作的视图模型的基类
 *
 * @author junjie
 */
@Init(superclass = true)
@AfterCompose(superclass = true)
public abstract class BaseTabsVM<T extends Component> extends BaseVM<T> {

    public static final String ZUL_FILE = "zul-file";
    private static final long serialVersionUID = -4482078341989184526L;

    private final Map<Object, Object> args = new HashMap<>();
    private final ConcurrentMap<Tab, Component> _panelsRef = new ConcurrentHashMap<>();

    @Override
    protected void doInit() {
        super.doInit();

        Map<?, ?> args = Executions.getCurrent().getArg();
        if (MapUtils.isNotEmpty(args)) {
            Set<?> keys = args.keySet();
            for (Object k : keys) {
                this.args.put(k, args.get(k));
            }
        }
    }

    @Override
    protected void doAfterCompose() {
        Tabbox _tb = getTabbox();
        Assert.notNull(_tb.getTabs(), "ZK Tabs mustn't be null.");

        // 绑定onSelect事件
        _tb.addEventListener(ON_SELECT, new SelectTabEventListener());

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

        _comp = getUiBuilder().manufacture(
            getUiParameterBuilder()
                .setUri(zulFile)
                .setArgs(this.args)
                .setParent(_selPanel)
                .build()
        );

        // 添加StepOut监听事件
        _comp.addEventListener(
            "onStepOut",
            new StepsOutEventListener(this, _selPanel)
        );

        // 添加引用关系
        this._panelsRef.putIfAbsent(_t, _comp);
    }

    /**
     * 获取TB组件对象
     */
    abstract protected Tabbox getTabbox();

    /**
     * 获取配置的<code>PreSufPathUriUiParameterBuilder</code>对象实例
     *
     * @return <code>PreSufPathUriUiParameterBuilder</code>
     */
    abstract protected PreSufPathUriUiParameterBuilder getUiParameterBuilder();

    /**
     * 选择Tab时触发的事件监听类
     */
    private class SelectTabEventListener implements SerializableEventListener<SelectEvent<Tab, Object>> {

        private static final long serialVersionUID = -4787108409412501457L;

        @Override
        public void onEvent(SelectEvent<Tab, Object> event) throws Exception {
            Set<Tab> unselectedTabs = event.getUnselectedItems();
            if (isNotEmpty(unselectedTabs)) {
                for (Tab _t : unselectedTabs) {
                    _unloadPanel(_t);
                }
            }

            Set<Tab> selectedTabs = event.getSelectedItems();
            if (isNotEmpty(selectedTabs)) {
                for (Tab _t : selectedTabs) {
                    _loadPanel(_t);
                }
            }
        }
    }
}
