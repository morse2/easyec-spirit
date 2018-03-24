package com.googlecode.easyec.zkex.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.ext.DynamicPropertied;

import java.io.Serializable;
import java.util.Map;

public class ShadowTemplate implements DynamicPropertied, Serializable {

    private boolean _autodrop;
    private Component _host;
    private ApplyEx _apply;

    public ShadowTemplate(boolean autodrop) {
        this._autodrop = autodrop;
        init();
    }

    private void init() {
        this._apply = new ApplyEx();
    }

    public String getTemplate() {
        return this._apply.getTemplate();
    }

    public void setTemplate(String template) {
        this._apply.setTemplate(template);
    }

    public void setTemplateURI(String templateURI) {
        this._apply.setTemplateURI(templateURI);
    }

    public String getTemplateURI() {
        return this._apply.getTemplateURI();
    }

    public boolean hasDynamicProperty(String name) {
        return this._apply.hasDynamicProperty(name);
    }

    public Object getDynamicProperty(String name) {
        return this._apply.getDynamicProperty(name);
    }

    public Map<String, Object> getDynamicProperties() {
        return this._apply.getDynamicProperties();
    }

    public void setDynamicProperty(String name, Object value) throws WrongValueException {
        this._apply.setDynamicProperty(name, value);
    }

    public Component getShadowHost() {
        return this._host;
    }

    public void apply(Component host) {
        if (this._autodrop) {
            applyDropTrue(host);
        } else {
            applyDropFalse(host);
        }
    }

    private void applyDropTrue(Component host) {
        if (host == null) {
            Component firstInsertion = _apply.getFirstInsertion();
            if (firstInsertion != null) {
                Component lastInsertion = _apply.getLastInsertion();
                Component next = firstInsertion;
                for (Component end = lastInsertion.getNextSibling(); next != end; ) {
                    Component tmp = next.getNextSibling();
                    next.detach();
                    next = tmp;
                }
            }

            this._apply.mergeSubTree();
            this._apply.detach();
            this._host = null;

            return;
        }

        if (this._host != null) {
            if (this._host != host) {
                throw new UiException("The shadow element cannot change its host, if existed. [" + this + "], please apply with null first!.");
            }
        } else {
            this._host = host;
            this._apply.setShadowHost(this._host, null);
        }

        if (this._apply.getAfterCompose()) {
            this._apply.recreate();
        } else {
            this._apply.afterCompose();
        }
    }

    private void applyDropFalse(Component host) {
        if (host == null) {
            throw new UiException("The shadow host cannot be null. [" + this + "].");
        }

        this._host = host;
        this._apply.setShadowHost(this._host, null);
        if (this._apply.getAfterCompose()) {
            this._apply.recreate();
        } else {
            this._apply.afterCompose();
        }
    }

    private class ApplyEx extends Apply {

        private static final long serialVersionUID = 3038389253237951911L;

        private ApplyEx() {}

        public void mergeSubTree() {
            super.mergeSubTree();
        }

        public boolean isDynamicValue() {
            return ShadowTemplate.this._autodrop;
        }

        private boolean getAfterCompose() {
            return this._afterComposed;
        }
    }
}
