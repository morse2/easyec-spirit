package com.googlecode.easyec.zkex.bind;

import com.googlecode.easyec.zkex.zul.TemplateBasedShadowElement;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Phase;
import org.zkoss.bind.PhaseListener;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.sys.Binding;
import org.zkoss.bind.sys.ReferenceBinding;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import java.util.List;

public class ZkexPhaseListener implements PhaseListener {

    public void prePhase(Phase phase, BindContext ctx) { /* no op */ }

    public void postPhase(Phase phase, BindContext ctx) {
        switch (phase) {
            case INITIAL_BINDING:
                if ((ctx.getComponent() instanceof ShadowElement)) {
                    Binder binder = ctx.getBinder();
                    if ((binder instanceof BinderImpl)) {
                        BinderImpl binderImpl = (BinderImpl) binder;
                        Component shadow = ctx.getComponent();
                        List<Binding> list = (List) binderImpl.getBindings(shadow).get(ctx.getCommandName());
                        boolean shallHandle = false;
                        for (Binding binding : list) {
                            if ((binding instanceof ReferenceBinding)) {
                                shallHandle = true;
                                break;
                            }
                        }
                        if (shallHandle) {
                            Execution current = Executions.getCurrent();
                            String key = Phase.LOAD_BINDING + shadow.getUuid();
                            if (!current.hasAttribute(key)) {
                                current.setAttribute(key, Boolean.TRUE);
                                ctx.setAttribute(shadow.getUuid(), key);
                                Events.postEvent(55536, TemplateBasedShadowElement.ON_BINDING_READY, shadow, ctx);
                            }
                        }
                    }
                }

                break;
            case LOAD_BINDING:
            case SAVE_BINDING:
                if ((ctx.getComponent() instanceof ShadowElement)) {
                    Component shadow = ctx.getComponent();
                    Execution current = Executions.getCurrent();
                    String key = phase + shadow.getUuid();
                    if (!current.hasAttribute(key)) {
                        current.setAttribute(key, Boolean.TRUE);
                        ctx.setAttribute(shadow.getUuid(), key);
                        if (shadow.getAttribute("$BINDRENDERING$") != null) {
                            Events.sendEvent(new Event(TemplateBasedShadowElement.ON_BINDING_READY, shadow, ctx));
                        } else
                            Events.postEvent(55536, TemplateBasedShadowElement.ON_BINDING_READY, shadow, ctx);
                    }
                }
        }
    }
}
