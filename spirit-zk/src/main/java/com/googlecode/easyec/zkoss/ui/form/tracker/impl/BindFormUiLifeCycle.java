package com.googlecode.easyec.zkoss.ui.form.tracker.impl;

import com.googlecode.easyec.zkoss.ui.util.UiLifeCycleAdapter;
import org.springframework.util.ReflectionUtils;
import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Form;
import org.zkoss.bind.impl.BindContextUtil;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.impl.BinderUtil;
import org.zkoss.bind.sys.Binding;
import org.zkoss.bind.sys.LoadBinding;
import org.zkoss.bind.sys.PropertyBinding;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.ShadowElement;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.zkoss.bind.BindComposer.BINDER_ID;
import static org.zkoss.bind.sys.BinderCtrl.*;

/**
 * 为<code>AnnotationBinder</code>重新绑定
 * 之前的Form代理对象的<code>UiLifeCycle</code>
 * 实现类
 *
 * @author junjie
 */
public class BindFormUiLifeCycle extends UiLifeCycleAdapter {

    /** 缓存的Form ID */
    protected static final String CACHED_FORM_ID = "$CACHED_FORM_ID$";
    /** 缓存的Form对象 */
    protected static final String CACHED_FORM = "$CACHED_FORM$";

    @Override
    public void afterComponentAttached(Component comp, Page page) {
        handleComponentAttached(comp);
    }

    @Override
    public void afterComponentDetached(Component comp, Page prevpage) {
        handleComponentDetached(comp);
    }

    @Override
    public void afterShadowAttached(ShadowElement shadow, Component host) {
        if (shadow instanceof Component) {
            handleComponentAttached(((Component) shadow));
        }
    }

    @Override
    public void afterShadowDetached(ShadowElement shadow, Component prevhost) {
        if (shadow instanceof Component) {
            handleComponentDetached(((Component) shadow));
        }
    }

    protected void handleComponentAttached(Component comp) {
        if (comp.getDesktop() != null || comp instanceof ShadowElement) {
            //check if this component already binded
            Binder selfBinder = BinderUtil.getBinder(comp);
            if (selfBinder == null) {
                //check if parent exists any binder
                Binder parentBinder = BinderUtil.getBinder(comp, true);
                if (parentBinder == null && comp instanceof ShadowElement) {
                    Component shadowHost = ((ShadowElement) comp).getShadowHost();
                    if (shadowHost != null)
                        parentBinder = BinderUtil.getBinder(shadowHost, true);
                }

                //post event to let the binder to handle binding later
                if ((parentBinder instanceof BinderImpl) || comp.hasAttribute(BINDER_ID)) {
                    comp.addEventListener(
                        9900, ON_BIND_INIT,
                        new ComponentAttachedEventListener()
                    );

                    //post ON_BIND_INIT event
                    Events.postEvent(new Event(ON_BIND_INIT, comp));
                }
            }
        }
    }

    protected void handleComponentDetached(Component comp) {
        comp.addEventListener(
            11000, ON_BIND_CLEAN,
            new ComponentDetachedEventListener()
        );

        Events.postEvent(new Event(ON_BIND_CLEAN, comp));
    }

    private class ComponentAttachedEventListener implements EventListener<Event> {

        @Override
        public void onEvent(Event event) throws Exception {
            final Component comp = event.getTarget();
            comp.removeEventListener(ON_BIND_INIT, this);

            if (comp.getPage() == null && !(comp instanceof ShadowElement)) {
                return;
            }

            Binder binder;
            String bid = (String) comp.getAttribute(BINDER_ID);
            if (bid != null) {
                binder = (Binder) comp.getAttribute(bid);
            } else {
                binder = BinderUtil.getBinder(comp, true);
            }

            if (binder == null) {
                if (comp instanceof ShadowElement) {
                    Component shadowHost = ((ShadowElement) comp).getShadowHost();
                    if (shadowHost != null)
                        binder = BinderUtil.getBinder(shadowHost, true);
                }
                if (binder == null)
                    return;
            }

            if (binder instanceof AnnotateBinder) {
                String formId = (String) comp.removeAttribute(CACHED_FORM_ID);
                if (isNotBlank(formId)) {
                    Object obj = comp.removeAttribute(CACHED_FORM);
                    if (obj instanceof Form) {
                        Form form = (Form) obj;
                        AnnotateBinder _binder = (AnnotateBinder) binder;
                        _binder.storeForm(comp, formId, form);

                        ReflectionUtils.doWithFields(BinderImpl.class, field -> {
                            ReflectionUtils.makeAccessible(field);
                            @SuppressWarnings("unchecked")
                            Map<Component, Map<String, List<Binding>>> _bindings
                                = (Map<Component, Map<String, List<Binding>>>)
                                ReflectionUtils.getField(field, _binder);

                            if (_bindings != null) {
                                processBindings(_bindings, _binder);
                            }
                        }, field -> "_bindings".equals(field.getName()));
                    }
                }
            }
        }

        private void processBindings(Map<Component, Map<String, List<Binding>>> _bindings, AnnotateBinder _binder) {
            _bindings.forEach((_c, _bs) -> _bs.forEach((_k, _ls) -> {
                List<Binding> list = _binder.getLoadPromptBindings(_c, _k);
                if (list != null) {
                    for (Binding binding : list) {
                        final BindContext ctx = BindContextUtil.newBindContext(
                            _binder, binding, false, null, _c, null
                        );

                        if (binding instanceof PropertyBinding) {
                            BindContextUtil.setConverterArgs(_binder, _c, ctx, (PropertyBinding) binding);
                        }

                        if (binding instanceof LoadBinding) {
                            ((LoadBinding) binding).load(ctx);
                        }
                    }
                }
            }));
        }
    }

    private class ComponentDetachedEventListener implements EventListener<Event> {

        @Override
        public void onEvent(Event event) throws Exception {
            final Component _target = event.getTarget();
            _target.removeEventListener(ON_BIND_CLEAN, this);
            String formId = (String) _target.getAttribute(FORM_ID);
            if (isNotBlank(formId)) {
                Object _form = _target.getAttribute(formId);
                if (_form instanceof Form) {
                    _target.setAttribute(CACHED_FORM_ID, formId);
                    _target.setAttribute(CACHED_FORM, _form);
                }
            }
        }
    }
}
