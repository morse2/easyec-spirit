package com.googlecode.easyec.zkoss.bind.proxy;

import javassist.util.proxy.MethodFilter;
import org.zkoss.bind.impl.AllocUtil;
import org.zkoss.bind.proxy.FormFieldCleaner;
import org.zkoss.bind.proxy.FormProxyHandler;
import org.zkoss.bind.proxy.FormProxyObject;
import org.zkoss.zk.ui.UiException;

import java.lang.reflect.Method;

/**
 * @author JunJie
 */
public class FormProxyHandlerEx<T> extends FormProxyHandler<T> {


    public FormProxyHandlerEx(T origin) {
        super(origin);
    }

    protected static MethodFilter FORMX_METHOD_FILTER = FormProxyHandler.FORM_METHOD_FILTER;

    @Override
    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Exception {
        final String mname = method.getName();
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.equals(FormFieldCleaner.class)) {
            return super.invoke(self, method, proceed, args);
        } else {
            if ("setFormOwner".equals(mname) || "getFormStatus".equals(mname)) {
                return super.invoke(self, method, proceed, args);
            }

            try {
                if (!method.getDeclaringClass().isAssignableFrom(FormProxyObject.class)) {
                    if (mname.startsWith("get")) {
                        if (_origin == null)
                            return null;

                        final String attr = toAttrName(method);
                        if (_cache != null) {
                            if (_cache.containsKey(attr)) {
                                return _cache.get(attr);
                            }
                        }

                        Object value = method.invoke(_origin, args);
                        if (value != null) {

                            // ZK-2736 Form proxy with Immutable values
                            value = ProxyHelper.createProxyIfAny(value, method.getAnnotations());

                            addCache(attr, value);
                            if (value instanceof FormProxyObject) {
                                addDirtyField(attr); // it may be changed.
                            }
                        }
                        return value;
                    }
                }
            } catch (Exception e) {
                throw UiException.Aide.wrap(e);
            }
        }

        return super.invoke(self, method, proceed, args);
    }

    private void addCache(String key, Object value) {
        _cache = AllocUtil.inst.putMap(_cache, key, value);
    }

    private void addDirtyField(String field) {
        _dirtyFieldNames = AllocUtil.inst.addSet(_dirtyFieldNames, field);
    }
}
