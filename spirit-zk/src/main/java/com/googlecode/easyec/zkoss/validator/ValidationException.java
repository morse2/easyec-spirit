package com.googlecode.easyec.zkoss.validator;

import org.zkoss.zk.ui.Component;

public class ValidationException extends Exception {

    private static final long serialVersionUID = -1077398971294679385L;
    private Component _comp;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public Component getComponent() {
        return _comp;
    }

    public void setComponent(Component comp) {
        this._comp = comp;
    }
}
