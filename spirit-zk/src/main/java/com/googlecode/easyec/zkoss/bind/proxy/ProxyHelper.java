package com.googlecode.easyec.zkoss.bind.proxy;

import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.annotation.Immutable;
import org.zkoss.bind.annotation.ImmutableElements;
import org.zkoss.bind.proxy.*;
import org.zkoss.zk.ui.UiException;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JunJie
 */
public class ProxyHelper {

    /**
     * Creates a proxy object from the given origin object, if any.
     *
     * @param origin
     * @param annotations the annotations of the caller method to indicate whether
     *                    the elements of the collection or Map type can proxy deeply, if any. (Optional)
     *                    Like {@link ImmutableElements}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Object> T createProxyIfAny(T origin, Annotation[] annotations) {
        if (origin == null)
            return null;
        if (origin instanceof FormProxyObject) {
            return origin;
        }
        if (annotations != null) {
            for (Annotation annot : annotations) {
                if (annot.annotationType().isAssignableFrom(Immutable.class))
                    return origin;
            }
        }
        if (org.zkoss.bind.proxy.ProxyHelper.isImmutable(origin))
            return origin;

        ProxyFactory factory = new ProxyFactory();
        if (origin instanceof List) {
            return (T) new ListProxy((List) origin, annotations);
        } else if (origin instanceof Set) {
            return (T) new SetProxy((Set) origin, annotations);
        } else if (origin instanceof Map) {
            return (T) new MapProxy((Map) origin, annotations);
        } else if (origin instanceof Collection) {
            return (T) new ListProxy((Collection) origin, annotations);
        } else if (origin.getClass().isArray()) {
            throw new UnsupportedOperationException("Array cannot be a proxy object!");
        } else {
            factory.setFilter(BeanProxyHandlerEx.BEANX_METHOD_FILTER);
            factory.setSuperclass(ClassUtils.getUserClass(origin));
            factory.setInterfaces(new Class[] { FormProxyObject.class });
            Class<?> proxyClass = factory.createClass();

            Object p1 = null;
            try {
                p1 = proxyClass.newInstance();
            } catch (Exception e) {
                throw UiException.Aide.wrap(e, "Cannot create a proxy object:[" + origin.getClass() + "], an empty constructor is needed.");
            }

            ((Proxy) p1).setHandler(new BeanProxyHandlerEx<T>(origin));
            return (T) p1;
        }
    }

    /**
     * Creates a proxy form object from the given origin object, if any.
     *
     * @param origin the origin data object
     * @param type   the class type of the data object
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Object> T createFormProxy(T origin, Class<?> type) {
        if (origin instanceof Form) return origin;

        ProxyFactory factory = new ProxyFactory();
        factory.setFilter(FormProxyHandlerEx.FORMX_METHOD_FILTER);
        if (origin instanceof FormProxyObject) {
            type = ((FormProxyObject) origin).getOriginObject().getClass();
        }

        factory.setSuperclass(type);
        factory.setInterfaces(new Class[] { FormProxyObject.class, Form.class, FormFieldCleaner.class });
        Class<?> proxyClass = factory.createClass();

        Object p1 = null;
        try {
            p1 = proxyClass.newInstance();
        } catch (Exception e) {
            throw UiException.Aide.wrap(e, "Cannot create a proxy object:[" + origin.getClass() + "], an empty constructor is needed.");
        }

        ((Proxy) p1).setHandler(new FormProxyHandlerEx<T>(origin));
        return (T) p1;
    }
}
