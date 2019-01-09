package com.googlecode.easyec.zkoss.ui.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class NotifyTabsEvent extends Event {

    public static final String NAME = "onNotifyTabs";
    private static final long serialVersionUID = -124878506511833930L;

    public NotifyTabsEvent() {
        super(NAME);
    }

    public NotifyTabsEvent(Component target) {
        super(NAME, target);
    }

    public NotifyTabsEvent(Component target, Object data) {
        super(NAME, target, data);
    }
}
