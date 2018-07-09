package com.googlecode.easyec.spirit.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.springframework.aop.support.AopUtils.*;

public final class ProxyUtils {

    private ProxyUtils() { }

    public static Class<?> getClass(Object o) {
        if (isCglibProxy(o) || isJdkDynamicProxy(o) || isAopProxy(o)) {
            return getTargetClass(o);
        } else if (o.getClass().getName().contains("_$$_")) {
            return o.getClass().getSuperclass();
        } else if (Proxy.isProxyClass(o.getClass())) {
            InvocationHandler _h = Proxy.getInvocationHandler(o);
            if (_h instanceof TargetProxy) return ((TargetProxy) _h).getTargetClass();
        }

        return o.getClass();
    }
}
