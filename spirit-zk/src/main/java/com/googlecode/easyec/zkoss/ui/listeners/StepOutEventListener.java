package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SerializableEventListener;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class StepOutEventListener implements SerializableEventListener<StepOutEvent> {

    private static final long serialVersionUID = 8671839842547838374L;
    private final Component _parent;

    public StepOutEventListener(Component parent) {
        Assert.notNull(parent, "Parent component is null.");
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
        // 设置当前组件无效
        event.getTarget().invalidate();
        removeChildren(_parent);
    }
}
