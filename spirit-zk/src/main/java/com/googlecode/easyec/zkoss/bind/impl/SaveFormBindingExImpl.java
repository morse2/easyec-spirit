package com.googlecode.easyec.zkoss.bind.impl;

import com.googlecode.easyec.zkoss.bind.proxy.ProxyHelper;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Form;
import org.zkoss.bind.impl.SaveFormBindingImpl;
import org.zkoss.bind.proxy.FormProxyObject;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ConditionType;
import org.zkoss.zk.ui.Component;

import java.util.Map;

/**
 * @author JunJie
 */
public class SaveFormBindingExImpl extends SaveFormBindingImpl {

    private static final long serialVersionUID = -5647554042576257071L;

    public SaveFormBindingExImpl(Binder binder, Component comp,
                                 String formId, String saveExpr,
                                 ConditionType conditionType, String command,
                                 Map<String, Object> bindingArgs, String validatorExpr,
                                 Map<String, Object> validatorArgs) {
        super(binder, comp, formId, saveExpr, conditionType, command, bindingArgs, validatorExpr, validatorArgs);
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
