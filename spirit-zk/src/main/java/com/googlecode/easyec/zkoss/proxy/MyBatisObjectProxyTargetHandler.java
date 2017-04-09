package com.googlecode.easyec.zkoss.proxy;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.javassist.util.proxy.Proxy;
import org.apache.ibatis.javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.zkoss.bind.proxy.ProxyTargetHandler;
import org.zkoss.zk.ui.UiException;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MyBatis代理对象的目标处理器类。
 * 该类负责桥接MVVM和MyBatis代理对象。
 *
 * @author JunJie
 * @since 0.7.2
 */
public class MyBatisObjectProxyTargetHandler implements ProxyTargetHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisObjectProxyTargetHandler.class);
    private final Object _lock = new Object();   // a lock here

    private boolean _init;
    private boolean _supported;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOriginObject(T origin) {
        synchronized (_lock) {
            if (!_isSupport()) return origin;

            if (ProxyFactory.isProxyClass(origin.getClass())) {
                Class<?>[] interfaces = ClassUtils.getAllInterfaces(
                    origin.getClass().getSuperclass()
                );

                List<Class<?>> all = new ArrayList<Class<?>>();
                if (ArrayUtils.isNotEmpty(interfaces)) {
                    all.addAll(Arrays.asList(interfaces));
                }

                javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
                factory.setInterfaces(all.toArray(new Class[all.size()]));
                factory.setSuperclass(origin.getClass().getSuperclass());

                try {
                    Object p = factory.createClass().newInstance();
                    ((javassist.util.proxy.Proxy) p).setHandler(
                        new ProxyHandlerBridge(((Proxy) origin))
                    );

                    return (T) p;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                    throw UiException.Aide.wrap(e,
                        "Cannot create a proxy bridge object:[" + origin.getClass()
                            + "], an empty constructor is needed."
                    );
                }
            }

            return origin;
        }
    }

    private boolean _isSupport() {
        if (!_init) {
            try {
                Class.forName("org.apache.ibatis.javassist.util.proxy.ProxyFactory");

                _supported = true;
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }

            _init = true;
        }

        return _supported;
    }

    private class ProxyHandlerBridge implements javassist.util.proxy.MethodHandler, Serializable {

        private static final long serialVersionUID = 6559439025622623599L;
        private Proxy delegate;

        ProxyHandlerBridge(Proxy delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
            return method.invoke(delegate, args);
        }
    }
}
