package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.Breadcrumb;
import com.googlecode.easyec.zkoss.ui.BreadcrumbCtrl;
import com.googlecode.easyec.zkoss.ui.events.BreadcrumbEvent;
import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.zkoss.bind.sys.BinderCtrl.VM;

public class StepOutEventListener implements SerializableEventListener<StepOutEvent> {

    private static final long serialVersionUID = 2288244560862948329L;
    private final Component _curComp;
    private final Component _parent;

    public StepOutEventListener(Component curComp, Component parent) {
        Assert.notNull(curComp, "Current component is null.");
        Assert.notNull(parent, "Parent component is null.");
        this._curComp = curComp;
        this._parent = parent;
    }

    public Component getParent() {
        return _parent;
    }

    @Override
    public void onEvent(StepOutEvent event) throws Exception {
        synchronized (_parent) {
            doStepOut(event);
        }
    }

    public void removeChildren(Component parent) {
        if (parent != null && isNotEmpty(parent.getChildren())) {
            parent.getChildren().clear();
        }
    }

    protected void doStepOut(StepOutEvent event) {
        event.getTarget().invalidate();
        removeChildren(_parent);
        _curComp.setParent(_parent);

        // 在必要的时候，通知面包屑变更事件
        Object vm = _curComp.getAttribute(VM);
        if (vm != null && vm instanceof BreadcrumbCtrl) {
            Breadcrumb _bc = ((BreadcrumbCtrl) vm).getBreadcrumb();
            if (_bc != null) Events.postEvent(new BreadcrumbEvent(_parent, _bc));
        }
    }
}
