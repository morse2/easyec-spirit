package com.googlecode.easyec.zkoss.ui.listeners;

import com.googlecode.easyec.zkoss.ui.Steps;
import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;

/**
 * @author junjie
 */
public class StepsOutEventListener extends StepOutEventListener {

    private static final long serialVersionUID = -4383612772901008187L;
    private Steps _step;

    public StepsOutEventListener(Steps step, Component parent) {
        super(parent);

        Assert.notNull(step, "Steps object cannot be null.");
        this._step = step;
    }

    public Steps getStep() {
        return _step;
    }

    @Override
    protected void doStepOut(StepOutEvent event) {
        super.doStepOut(event);

        this._step.stepOut();
    }
}
