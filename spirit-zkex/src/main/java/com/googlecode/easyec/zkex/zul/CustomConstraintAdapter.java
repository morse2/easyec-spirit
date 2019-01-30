package com.googlecode.easyec.zkex.zul;

import com.googlecode.easyec.zkoss.viewmodel.ViewModelAware;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.SaveBinding;
import org.zkoss.bind.sys.SavePropertyBinding;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.CustomConstraint;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

public interface CustomConstraintAdapter extends CustomConstraint {

    ViewModelAware<?> getVM();

    @Override
    default void showCustomError(Component comp, WrongValueException e) {
        BinderCtrl binder = (BinderCtrl) getVM().getBinder();
        SaveBinding saveBinding
            = binder.getFormAssociatedSaveBindings(getVM().getSelf()).stream()
            .filter(binding -> StringUtils.equals(binding.getComponent().getId(), comp.getId()))
            .findFirst()
            .orElse(null);
        Assert.notNull(saveBinding, "SaveBinding mustn't be null.");

        ValidationMessages vmsgs = binder.getValidationMessages();
        if (isNotEmpty(vmsgs.getMessages(comp))) {
            vmsgs.clearMessages(comp);
        }

        if (e != null) {
            String attr = ((SavePropertyBinding) saveBinding).getFieldName();
            vmsgs.addMessages(comp, attr, null, new String[] { e.getMessage() });
            BindUtils.postNotifyChange(null, null, vmsgs, ".");
        }
    }
}
