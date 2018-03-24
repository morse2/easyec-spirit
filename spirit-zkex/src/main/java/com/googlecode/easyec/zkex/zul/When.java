package com.googlecode.easyec.zkex.zul;

import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;

public class When extends If {

    private static final long serialVersionUID = -5405089950371161203L;

    public When() {
        this._afterComposed = true;
    }

    public Object setAttribute(String name, Object value) {
        if (BinderCtrl.BINDER.equals(name)) {
            getParent().setAttribute(name, value);
        }

        return super.setAttribute(name, value);
    }

    public Object removeAttribute(String name) {
        Object result = super.removeAttribute(name);

        if ((BinderCtrl.BINDER.equals(name)) && (getParent() != null)) {
            boolean hasAttr = false;
            for (Component child : getParent().getChildren()) {
                if (child.hasAttribute(name)) {
                    hasAttr = true;
                    break;
                }
            }
            if (!hasAttr)
                getParent().removeAttribute(name);
        }
        return result;
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

    public void beforeParentChanged(Component parent) {
        if ((parent != null) && (!(parent instanceof Choose))) {
            throw new UiException("Unsupported parent for otherwise: " + parent);
        }

        super.beforeParentChanged(parent);
    }
}
