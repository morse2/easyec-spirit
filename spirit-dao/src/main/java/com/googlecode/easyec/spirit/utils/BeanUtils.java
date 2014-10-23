package com.googlecode.easyec.spirit.utils;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.select;
import static org.apache.commons.lang.ArrayUtils.isEmpty;

/**
 * 针对于Bean代理的工具类
 *
 * @author JunJie
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    private static final Object object = new Object();

    private BeanUtils() { /* no op */ }

    /**
     * 解析基于给定的目标类型所描述的泛型类型的数据对象。
     * 如果给定的目标对象不包含泛型定义，则返回null。
     * 如果索引参数超出了目标对象定义的泛型个数，则抛出
     * 相应的异常信息。
     *
     * @param bean       解析的Bean对象
     * @param targetType 目标对象类型
     * @param index      泛型类型的索引号
     * @return 匹配的泛型描述类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> findGenericType(Object bean, Class targetType, int index) {
        if (bean == null || targetType == null || index < 0) return null;

        synchronized (object) {
            TypeVariable<? extends Class<?>>[] targetTypeParameters = targetType.getTypeParameters();
            if (isEmpty(targetTypeParameters)) {
                logger.warn("No type of parameters for target class.");

                return null;
            }

            TypeVariable<? extends Class<?>> targetTypeParameter;

            try {
                targetTypeParameter = targetTypeParameters[index];
            } catch (IndexOutOfBoundsException e) {
                logger.error(e.getMessage(), e);

                return null;
            }

            Class sourceCls = bean.getClass();
            if (AopUtils.isAopProxy(bean)) {
                sourceCls = AopUtils.getTargetClass(bean);
            }

            List<Class> classTree
                = targetType.isInterface()
                ? _getInterfaceList(sourceCls, targetType)
                : _getClassTree(bean.getClass(), targetType);

            for (int i = classTree.size() - 1; i >= 0; i--) {
                List<Type> sourceTypes = new ArrayList<Type>();
                if (targetType.isInterface()) {
                    sourceTypes.addAll(Arrays.asList(classTree.get(i).getGenericInterfaces()));
                } else sourceTypes.add(classTree.get(i).getGenericSuperclass());

                for (Type sourceType : sourceTypes) {
                    if (!(sourceType instanceof ParameterizedType)) continue;

                    Class<?> type = _matchesTargetType(
                        (ParameterizedType) sourceType,
                        targetTypeParameter
                    );

                    if (type != null) return (Class<T>) type;
                }
            }

            return null;
        }
    }

    /* 获取源对象的继承结构 */
    private static List<Class> _getClassTree(Class<?> cls, Class<?> targetType) {
        List<Class> list = new ArrayList<Class>();

        do {
            list.add(cls);
            cls = cls.getSuperclass();
        } while (cls != null && !cls.isAssignableFrom(targetType));

        return list;
    }

    /* 获取源对象的接口结构 */
    @SuppressWarnings("unchecked")
    private static List<Class> _getInterfaceList(Class<?> cls, final Class<?> targetType) {
        return new ArrayList<Class>(
            select(
                ClassUtils.getAllInterfacesForClassAsSet(cls),
                new Predicate() {

                    public boolean evaluate(Object obj) {
                        return targetType.isAssignableFrom(((Class) obj))
                            && !targetType.equals((obj));
                    }
                }
            )
        );
    }

    /* 源参数类型对象匹配目标类型的方法 */
    private static Class<?> _matchesTargetType(ParameterizedType sourceType, TypeVariable<? extends Class<?>> targetTypeParameter) {
        Type[] actualTypeArguments = sourceType.getActualTypeArguments();
        Type[] bounds = targetTypeParameter.getBounds();
        for (Type bound : bounds) {
            if (!(bound instanceof ParameterizedType)) continue;

            for (Type type : actualTypeArguments) {
                if (!(type instanceof Class)) continue;

                boolean b = ((Class<?>) ((ParameterizedType) bound).getRawType())
                    .isAssignableFrom(((Class) type));

                if (b) return (Class<?>) type;
            }
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
