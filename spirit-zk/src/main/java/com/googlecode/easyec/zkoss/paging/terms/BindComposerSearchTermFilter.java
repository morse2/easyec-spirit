package com.googlecode.easyec.zkoss.paging.terms;

import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;

/**
 * 过滤<code>BindComposer</code>
 * 对象中的属性。
 *
 * @author JunJie
 */
public class BindComposerSearchTermFilter implements SearchTermsFilter {

    protected static final String $BINDER_ID$ = "$BINDER_ID$";
    protected static final String BINDER_ATTR = "binder";
    protected static final String $VM_ID$     = "$VM_ID$";
    protected static final String VM_ATTR     = "vm";

    protected static final String $BinderKeeper$ = "$BinderKeeper$";
    protected static final String _$COMPOSER$_   = "_$composer$_";
    protected static final String $COMPOSER      = "$composer";
    protected static final String $VM$           = "$VM$";
    protected static final String $BINDER$       = "$BINDER$";

    protected static final String $ACTIVATOR$   = "$ACTIVATOR$";
    protected static final String $BindComposer = "$BindComposer";

    protected static final String COMBOBOX_ON_INITRENDER_ATTR = "zul.Combobox.ON_INITRENDER";

    public boolean accept(String k, Object v) {
        return !($BINDER_ID$.equals(k)
            || BINDER_ATTR.equals(k)
            || $VM_ID$.equals(k)
            || VM_ATTR.equals(k)
            || $BinderKeeper$.equals(k)
            || _$COMPOSER$_.equals(k)
            || $COMPOSER.equals(k)
            || $VM$.equals(k)
            || $BINDER$.equals(k)
            || $ACTIVATOR$.equals(k)
            || $BindComposer.equals(k)
            || COMBOBOX_ON_INITRENDER_ATTR.equals(k));
    }
}
