package com.googlecode.easyec.zkex.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

public class Otherwise extends TemplateBasedShadowElement {

    private static final long serialVersionUID = 6735205805121479437L;

    public Otherwise() {
        this._afterComposed = true;
    }

    public void beforeParentChanged(Component parent) {
        if ((parent != null) && (!(parent instanceof Choose))) {
            throw new UiException("Unsupported parent for otherwise: " + parent);
        }

        super.beforeParentChanged(parent);
    }

    protected boolean isEffective() {
        return true;
    }

    public void recreate() {
        if (isBindingReady()) {
            Choose choose = (Choose) getParent();
            choose.triggerRecreateEvent();
        } else {
            Choose choose = (Choose) getParent();
            if (choose != null) {
                choose.recreate();
            }
        }
    }

    protected void recreateDirectly() {
        super.recreate();
    }
}
