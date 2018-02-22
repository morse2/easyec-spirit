package com.googlecode.easyec.zkoss.ui.events;

import com.googlecode.easyec.zkoss.ui.Breadcrumb;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class BreadcrumbEvent extends Event {

    public static final String ON_CRUMB_CHANGE = "onCrumbChange";
    private static final long serialVersionUID = 4657354552975536506L;

    public BreadcrumbEvent(Component target, Object data) {
        super(ON_CRUMB_CHANGE, target, data);
    }

    public Breadcrumb getCrumb() {
        return (Breadcrumb) getData();
    }
}
