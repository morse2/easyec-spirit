package com.googlecode.easyec.zkoss.ui.events;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Event;

import java.util.Map;

/**
 * <code>PopState</code> for client
 * state of user's browser.
 *
 * @author junjie
 */
public class PopStateEvent extends Event {

    private static final long serialVersionUID = 7267560333193919118L;
    private final Map<String, ?> _state;

    public PopStateEvent(String name, Map<String, ?> state) {
        super(name);
        this._state = state;
    }

    public Map<String, ?> getState() {
        return _state;
    }

    public static PopStateEvent getPopStateEvent(AuRequest request) {
        return new PopStateEvent(request.getCommand(), request.getData());
    }
}
