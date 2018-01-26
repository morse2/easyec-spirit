package com.googlecode.easyec.zkoss.ui.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class StepEvent extends Event {

    private static final long serialVersionUID = 7096523007870075043L;

    public StepEvent(String name) {
        super(name);
    }

    public StepEvent(String name, Component target) {
        super(name, target);
    }

    public StepEvent(String name, Component target, Object data) {
        super(name, target, data);
    }
}
