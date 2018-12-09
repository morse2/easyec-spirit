package com.googlecode.easyec.spirit.mybatis.mapper.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

public class DaoMapperUtils {

    private static final Logger logger = LoggerFactory.getLogger(DaoMapperUtils.class);

    private DaoMapperUtils() {}

    public static boolean isProxy(Object obj) {
        return Proxy.isProxyClass(obj.getClass())
            && (Proxy.getInvocationHandler(obj) instanceof DaoMapperProxy);
    }

    public static Class<?> getDomainModelClass(Object obj) {
        if (!isProxy(obj)) return null;

        try {
            return ((DaoMapperProxy) Proxy.getInvocationHandler(obj))
                .getDomainModelClass();
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
