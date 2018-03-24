package com.googlecode.easyec.zkex.zul;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.BooleanPropertyAccess;
import org.zkoss.zk.ui.sys.PropertyAccess;

import java.util.HashMap;

public class If extends TemplateBasedShadowElement {

    private static final long serialVersionUID = 8394272523619123992L;
    private boolean _test;
    private static HashMap<String, PropertyAccess> _properties = new HashMap<>(1);

    static {
        _properties.put("test", new BooleanPropertyAccess() {

            private static final long serialVersionUID = -738571547643916232L;

            public void setValue(Component cmp, Boolean value) {
                ((If) cmp).setTest(value);
            }

            public Boolean getValue(Component cmp) {
                return ((If) cmp).isTest();
            }
        });
    }

    public void setTest(boolean test) {
        if (this._test != test) {
            this._test = test;
            this._dirtyBinding = true;
        }
    }

    public boolean getTest() {
        return isTest();
    }

    public boolean isTest() {
        return this._test;
    }

    protected boolean isEffective() {
        return getTest();
    }

    public PropertyAccess getPropertyAccess(String prop) {
        PropertyAccess pa = _properties.get(prop);
        return null == pa
            ? super.getPropertyAccess(prop)
            : pa;
    }
}
