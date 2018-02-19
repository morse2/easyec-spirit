package com.googlecode.easyec.zkoss.ui.pushstate;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.DesktopInit;

public class PushStateDesktopInit implements DesktopInit {

    @Override
    public void init(Desktop desktop, Object request) throws Exception {
        desktop.addListener(new PushStateAuService());
    }
}
