package com.googlecode.easyec.zkex.zul;

import org.zkoss.bind.BindContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlShadowElement;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.sys.ComponentCtrl;
import org.zkoss.zk.ui.util.Template;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;

import static org.zkoss.bind.sys.BinderCtrl.ON_BIND_INIT;

public abstract class TemplateBasedShadowElement extends HtmlShadowElement {

    private static final long serialVersionUID = 1615490094616965796L;
    public static String ON_BINDING_READY = "onBindingReady";
    public static String SHADOW_VARIABLE = "$ShadowVariable$";
    public static final String FOREACH_RENDERED_COMPONENTS = "$ForEachRenderedComponents$";

    private boolean _bindingReady = false;
    protected boolean _dirtyBinding = true;

    public TemplateBasedShadowElement() {
        init();
    }

    private void init() {
        addBindingListener();
    }

    public void onChildRemoved(Component child) {
        if ((child instanceof TemplateBasedShadowElement)) {
            ((TemplateBasedShadowElement) child).removeBindingListener();
        }

        super.onChildRemoved(child);
    }

    public void onChildAdded(Component child) {
        if ((child instanceof TemplateBasedShadowElement)) {
            ((TemplateBasedShadowElement) child).addBindingListener();
        }

        super.onChildAdded(child);
    }

    public void afterCompose() {
        if (!_afterComposed) {
            if (null == getFirstInsertion()) {
                super.afterCompose();

                if (null != getFirstInsertion()) {
                    List<Component> distributedChildren = getDistributedChildren();

                    if (_bindingReady) {
                        for (Component comp : distributedChildren) {
                            Events.sendEvent(new Event(ON_BIND_INIT, comp));
                        }

                        for (Component shadow : getChildren()) {
                            Events.sendEvent(new Event(ON_BIND_INIT, shadow));
                        }
                    }
                }
            }

            this._afterComposed = true;
        }
    }

    public boolean isDynamicValue() {
        if (null != _dynamicValue && _dynamicValue) {
            return true;
        }

        if (getParent() instanceof ForEach) {
            if ((hasBindingAnnotation()) || (hasSubBindingAnnotation())) {
                return true;
            }

            Component next = getFirstInsertion();
            while (null != next) {
                ComponentCtrl nextCtrl = (ComponentCtrl) next;
                if (nextCtrl.hasBindingAnnotation() || nextCtrl.hasSubBindingAnnotation()) {
                    return true;
                }

                if (next == getLastInsertion()) {
                    break;
                }

                next = next.getNextSibling();
            }
        }

        return super.isDynamicValue();
    }

    protected void compose(Component host) {
        Template _t = getTemplate("");
        if (null != _t) {
            _t.create(
                host, getNextInsertionComponentIfAny(),
                null, null
            );
        }
    }

    @Override
    protected void rebuildSubShadowTree() {
        super.rebuildSubShadowTree();
    }

    protected void addBindingListener() {
        if (!isListenerAvailable(ON_BINDING_READY, false)) {
            addEventListener(ON_BINDING_READY, (SerializableEventListener<Event>) this::_doBindingReady);
        }
    }

    protected void removeBindingListener() {
        Iterable<EventListener<? extends Event>> eventListeners = getEventListeners(ON_BINDING_READY);
        for (Iterator<EventListener<? extends Event>> it = eventListeners.iterator(); it.hasNext(); )
            it.remove();
    }

    public void detach() {
        removeBindingListener();
        super.detach();
    }


    protected boolean isBindingReady() {
        return this._bindingReady;
    }

    protected boolean isEffective() {
        return false;
    }

    public Object clone() {
        TemplateBasedShadowElement clone = (TemplateBasedShadowElement) super.clone();
        clone.init();
        return clone;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        init();
    }

    private void _doBindingReady(Event event) {
        _bindingReady = true;

        if ((event.getData() instanceof BindContext)) {
            BindContext ctx = (BindContext) event.getData();
            String _key = (String) ctx.getAttribute(event.getTarget().getUuid());
            Executions.getCurrent().removeAttribute(_key);
        }

        if (_dirtyBinding) {
            _dirtyBinding = false;

            Component host = getShadowHostIfAny();
            if (null != host && null != host.getDesktop()) {
                recreate();
            } else removeBindingListener();
        }
    }
}
