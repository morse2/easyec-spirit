package com.googlecode.easyec.zkex.bind.impl;

import org.zkoss.bind.impl.AbstractBindingHandler;
import org.zkoss.bind.impl.ReferenceBindingHandler;
import org.zkoss.bind.sys.ReferenceBinding;
import org.zkoss.zk.ui.Component;

import java.util.HashSet;
import java.util.Set;

import static org.zkoss.zk.ui.Component.COMPONENT_SCOPE;

@SuppressWarnings("unchecked")
public class ReferenceBindingHandlerImpl extends AbstractBindingHandler implements ReferenceBindingHandler {

    private static final String REFERENCE_SET = "$REF_SET$";
    private static final long serialVersionUID = 270556140703904367L;

    public void addReferenceBinding(Component comp, String attr, ReferenceBinding binding) {
        comp.setAttribute(attr, binding);

        Set<String> refs = (Set<String>) comp.getAttribute(REFERENCE_SET, COMPONENT_SCOPE);
        if (null == refs) {
            refs = new HashSet<>();
            comp.setAttribute(REFERENCE_SET, refs);
        }

        refs.add(attr);
    }

    public void removeReferenceBinding(Component comp) {
        Set<String> refs = (Set<String>) comp.removeAttribute(REFERENCE_SET);
        if (null != refs) {
            for (String ref : refs) {
                comp.removeAttribute(ref);
            }
        }
    }

    public void removeReferenceBinding(Component comp, String attr) {
        Set<String> refs = (Set) comp.getAttribute(REFERENCE_SET, COMPONENT_SCOPE);
        if (null != refs) {
            if (refs.remove(attr)) {
                comp.removeAttribute(attr);
            }

            if (refs.size() == 0) {
                comp.removeAttribute(REFERENCE_SET);
            }
        }
    }
}