package com.googlecode.easyec.spirit.utils;

import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 针对于Bean代理的工具类
 *
 * @author JunJie
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    private static final Object object = new Object();

    private BeanUtils() { }

    /**
     * 解析泛型类型数据对象
     *
     * @param index 泛型类型的索引号
     * @return 具体的实现类
     */
    public static Class getGenericType(Object bean, int index) {
        synchronized (object) {
            Class cls = bean.getClass();
            if (AopUtils.isAopProxy(bean)) {
                cls = AopUtils.getTargetClass(bean);
            }

            Type genericType = findSuperclassType(cls);

            if (!(genericType instanceof ParameterizedType)) {
                String msg = cls.getSimpleName() + "'s superclass isn't class ParameterizedType";
                logger.error(msg);

                throw new IllegalArgumentException(msg);
            }

            Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();

            if (index >= params.length || index < 0) {
                String msg = "Index: " + index + ", Size of " + cls.getSimpleName() +
                    "'s Parameterized Type: " + params.length;

                logger.error(msg);

                throw new IllegalArgumentException(msg);
            }

            Type param = params[index];
            return null == param ? null : (param instanceof Class) ? (Class) param : param.getClass();
        }
    }

    private static Type findSuperclassType(Class cls) {
        Type genType = cls.getGenericSuperclass();

        while (genType instanceof Class) {
            if (null == ((Class) genType).getSuperclass()) break;
            genType = ((Class) genType).getGenericSuperclass();
        }

        if (genType instanceof Class) {
            genType = findInAllInterfaces(cls.getInterfaces());
        }

        return genType;
    }

    private static Type findInAllInterfaces(Class[] interfaces) {
        for (Class ifs : interfaces) {
            Type type = findSuperInterfaceType(ifs.getGenericInterfaces());
            if (null == type) type = findInAllInterfaces(ifs.getInterfaces());
            if (null != type) return type;
        }

        return null;
    }

    private static Type findSuperInterfaceType(Type[] types) {
        for (Type type : types) {
            if (!(type instanceof Class)) return type;

            Type thisType = findSuperInterfaceType(((Class) type).getGenericInterfaces());
            if (null != thisType) return thisType;
        }

        return null;
    }

    /**
     * 查找目标对象中的字段属性值，
     * 并且强制转换成给定的类型。
     *
     * @param target    目标对象实例
     * @param fieldName 字段名称
     * @param cls       类对象
     * @param <T>       泛型
     * @return 目标对象中的属性对象
     */
    public static <T> T readField(Object target, String fieldName, Class<T> cls) throws IllegalAccessException {
        try {
            Object o = FieldUtils.readDeclaredField(target, fieldName, true);
            if (null == o) return null;
            if (!cls.isInstance(o)) {
                logger.trace("The object [{}] isn't instance of [{}].", o.getClass().getName(), cls.getName());

                return null;
            }

            return cls.cast(o);
        } catch (Exception e) {
            logger.trace(e.getMessage(), e);

            Object o = FieldUtils.readField(target, fieldName, true);
            if (null == o) return null;
            if (!cls.isInstance(o)) {
                logger.trace("The object [{}] isn't instance of [{}].", o.getClass().getName(), cls.getName());

                return null;
            }

            return cls.cast(o);
        }
    }
}
