package com.googlecode.easyec.zkoss.ui.events;

import org.zkoss.zk.ui.Component;

public class StepOutEvent extends StepEvent {

    private static final long serialVersionUID = 4486261992738054994L;

    public StepOutEvent() {
        super("onStepOut");
    }

    public StepOutEvent(Component target) {
        super("onStepOut", target);
    }

    public StepOutEvent(Component target, Object data) {
        super("onStepOut", target, data);
    }
}
