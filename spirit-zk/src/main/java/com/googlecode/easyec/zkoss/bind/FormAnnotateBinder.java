package com.googlecode.easyec.zkoss.bind;

import com.googlecode.easyec.zkoss.bind.impl.LoadFormBindingExImpl;
import com.googlecode.easyec.zkoss.bind.impl.SaveFormBindingExImpl;
import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.sys.ConditionType;
import org.zkoss.bind.sys.LoadFormBinding;
import org.zkoss.bind.sys.SaveFormBinding;
import org.zkoss.zk.ui.Component;

import java.util.Map;

/**
 * 为MVVM表单解析提供扩展功能的类
 *
 * @author JunJie
 */
public class FormAnnotateBinder extends AnnotateBinder {

    private static final long serialVersionUID = -1282632921203481574L;

    public FormAnnotateBinder() {
        super();
    }

    public FormAnnotateBinder(String qname, String qscope) {
        super(qname, qscope);
    }

    @Override
    protected LoadFormBinding newLoadFormBinding(Component comp, String formId, String loadExpr,
                                                 ConditionType conditionType, String command,
                                                 Map<String, Object> bindingArgs) {
        return new LoadFormBindingExImpl(this, comp, formId, loadExpr, conditionType, command, bindingArgs);
    }

    @Override
    protected SaveFormBinding newSaveFormBinding(Component comp, String formId, String saveExpr,
                                                 ConditionType conditionType, String command,
                                                 Map<String, Object> bindingArgs, String validatorExpr,
                                                 Map<String, Object> validatorArgs) {
        return new SaveFormBindingExImpl(this, comp, formId, saveExpr,
            conditionType, command, bindingArgs, validatorExpr,
            validatorArgs);
    }
}
