package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.Breadcrumb;
import com.googlecode.easyec.zkoss.ui.BreadcrumbCtrl;
import com.googlecode.easyec.zkoss.ui.events.BreadcrumbEvent;
import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;

import static org.zkoss.bind.sys.BinderCtrl.VM;

/**
 * 跳出到上一层页面的事件监听类
 *
 * @author junjie
 */
public class StepOutUpperEventListener extends StepOutEventListener {

    private static final long serialVersionUID = -5023480876220436267L;
    private Component _uppComp;

    public StepOutUpperEventListener(Component uppComp) {
        this(uppComp, uppComp.getParent());
    }

    public StepOutUpperEventListener(Component uppComp, Component parent) {
        super(parent);

        Assert.notNull(uppComp, "Upper component cannot be null.");
        this._uppComp = uppComp;
    }

    public Component getUpper() {
        return _uppComp;
    }

    @Override
    protected void doStepOut(StepOutEvent event) {
        super.doStepOut(event);
        // 设置上一层组件可见
        _uppComp.setParent(getParent());
        // 在必要的时候，通知面包屑变更事件
        rebuildBreadcrumb(_uppComp);
    }

    /**
     * 重构给定组件的面包屑
     */
    protected void rebuildBreadcrumb(Component comp) {
        Object vm = comp.getAttribute(VM);
        if (vm instanceof BreadcrumbCtrl) {
            Breadcrumb _bc = ((BreadcrumbCtrl) vm).getBreadcrumb();
            if (_bc != null) Events.postEvent(new BreadcrumbEvent(getParent(), _bc));
        }
    }
}
