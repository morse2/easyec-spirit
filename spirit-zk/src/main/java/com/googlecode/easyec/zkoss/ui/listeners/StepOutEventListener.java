package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SerializableEventListener;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class StepOutEventListener implements SerializableEventListener<StepOutEvent> {

    private static final long serialVersionUID = -635437173380705895L;
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
            event.getTarget().invalidate();
            _removeChildrenOfParent();
            _curComp.setParent(_parent);
        }
    }

    private void _removeChildrenOfParent() {
        List<Component> children = getParent().getChildren();
        if (isNotEmpty(children)) children.clear();
    }
}
