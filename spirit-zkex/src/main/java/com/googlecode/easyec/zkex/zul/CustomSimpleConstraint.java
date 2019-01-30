package com.googlecode.easyec.zkex.zul;

import com.googlecode.easyec.zkoss.viewmodel.ViewModelAware;
import org.springframework.util.Assert;
import org.zkoss.zul.SimpleConstraint;

import java.util.regex.Pattern;

public class CustomSimpleConstraint extends SimpleConstraint implements CustomConstraintAdapter {

    private static final long serialVersionUID = 6574675094366655014L;
    private ViewModelAware<?> vm;

    public CustomSimpleConstraint(ViewModelAware<?> vm, int flags) {
        super(flags);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, int flags, String errmsg) {
        super(flags, errmsg);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, String regex, String errmsg) {
        super(regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, Pattern regex, String errmsg) {
        super(regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, int flags, String regex, String errmsg) {
        super(flags, regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, int flags, Pattern regex, String errmsg) {
        super(flags, regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleConstraint(ViewModelAware<?> vm, String constraint) {
        super(constraint);
        _init0(vm);
    }

    @Override
    public ViewModelAware<?> getVM() {
        return this.vm;
    }

    private void _init0(ViewModelAware<?> vm) {
        Assert.notNull(vm, "ViewModelAware cannot be null.");
        this.vm = vm;
    }
}
