package com.googlecode.easyec.zkex.zul;

import com.googlecode.easyec.zkoss.viewmodel.ViewModelAware;
import org.springframework.util.Assert;
import org.zkoss.zul.SimpleDateConstraint;

import java.util.Date;
import java.util.regex.Pattern;

public class CustomSimpleDateConstraint extends SimpleDateConstraint implements CustomConstraintAdapter {

    private static final long serialVersionUID = 5004012004237592815L;
    private ViewModelAware<?> vm;

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, int flags) {
        super(flags);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, int flags, String errmsg) {
        super(flags, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, String regex, String errmsg) {
        super(regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, Pattern regex, String errmsg) {
        super(regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, int flags, String regex, String errmsg) {
        super(flags, regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, int flags, Pattern regex, String errmsg) {
        super(flags, regex, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, int flags, Date begin, Date end, String errmsg) {
        super(flags, begin, end, errmsg);
        _init0(vm);
    }

    public CustomSimpleDateConstraint(ViewModelAware<?> vm, String constraint) {
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
