package com.googlecode.easyec.zkoss.ui.pushstate;

import org.zkoss.zk.ui.Component;

/**
 * @author junjie
 */
public class DefaultPopState implements PopState {

    private static final long serialVersionUID = -4821260726416691420L;
    private Component _comp;

    public DefaultPopState(Component comp) {
        this._comp = comp;
    }

    @Override
    public Component getComponent() {
        return this._comp;
    }
}
