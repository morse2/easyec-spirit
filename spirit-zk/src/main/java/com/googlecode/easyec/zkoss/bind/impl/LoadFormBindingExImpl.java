package com.googlecode.easyec.zkoss.bind.impl;

import com.googlecode.easyec.zkoss.bind.proxy.ProxyHelper;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Form;
import org.zkoss.bind.impl.LoadFormBindingImpl;
import org.zkoss.bind.proxy.FormProxyObject;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ConditionType;
import org.zkoss.zk.ui.Component;

import java.util.Map;

/**
 * @author JunJie
 */
public class LoadFormBindingExImpl extends LoadFormBindingImpl {

    private static final long serialVersionUID = -3034513607697483926L;

    public LoadFormBindingExImpl(Binder binder, Component comp, String formId, String loadExpr,
                                 ConditionType conditionType, String command,
                                 Map<String, Object> bindingArgs) {
        super(binder, comp, formId, loadExpr, conditionType, command, bindingArgs);
    }

    @Override
    public <T> Form initFormBean(Object bean, Class<Object> class1) {
        Form form = ((BinderCtrl) getBinder()).getForm(getComponent(), _formId);
        if (form == null) {
            form = (Form) ProxyHelper.createFormProxy(bean, ClassUtils.getUserClass(class1));
            ((BinderCtrl) getBinder()).storeForm(getComponent(), _formId, form);
        }
        if (!(bean instanceof Form) && form instanceof FormProxyObject)
            ((FormProxyObject) form).setFormOwner(bean, this);
        return form;
    }
}
