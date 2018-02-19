package com.googlecode.easyec.zkoss.ui.pushstate;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuService;
import org.zkoss.zk.ui.event.Events;

import static com.googlecode.easyec.zkoss.ui.events.PopStateEvent.getPopStateEvent;

public class PushStateAuService implements AuService {

    private static final String COMMAND_NAME = "onPopState";

    @Override
    public boolean service(AuRequest request, boolean everError) {
        if (COMMAND_NAME.equals(request.getCommand())) {
            Events.postEvent(getPopStateEvent(request));

            return true;
        }

        return false;
    }
}
