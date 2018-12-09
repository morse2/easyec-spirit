package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

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
     * 解析泛型类型数据对象
     *
     * @param o     对象实例
     * @param index 泛型类型的索引号
     * @return 具体的实现类
     */
    public static Class findGenericType(Object o, int index) {
        return findGenericType(o.getClass(), index);
    }

    /**
     * 解析泛型类型数据对象
     *
     * @param o     对象类型
     * @param index 泛型类型的索引号
     * @return 具体的实现类
     */
    public static Class findGenericType(Class<?> o, int index) {
        Type genType = o.getGenericSuperclass();

        while (genType instanceof Class) {
            if (null == ((Class) genType).getSuperclass()) {
                String msg = "No superclass was found. [" + o.getName() + "].";
                logger.error(msg);

                throw new IllegalArgumentException(msg);
            }

            genType = ((Class) genType).getGenericSuperclass();
        }

        if (!(genType instanceof ParameterizedType)) {
            String msg = o.getSimpleName() + "'s superclass isn't class ParameterizedType";
            logger.error(msg);

            throw new IllegalArgumentException(msg);
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            String msg = "Index: " + index + ", Size of " + o.getSimpleName() +
                "'s Parameterized Type: " + params.length;

            logger.warn(msg);

            return null;
        }

        Type param = params[index];

        if (param instanceof Class) {
            return (Class) param;
        }

        return param.getClass();
    }

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
                List<Type> sourceTypes = new ArrayList<>();
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
        List<Class> list = new ArrayList<>();

        do {
            list.add(cls);
            cls = cls.getSuperclass();
        } while (cls != null && !cls.isAssignableFrom(targetType));

        return list;
    }

    /* 获取源对象的接口结构 */
    private static List<Class> _getInterfaceList(Class<?> cls, final Class<?> targetType) {
        return ClassUtils.getAllInterfacesForClassAsSet(cls)
            .stream().filter(clz
                -> targetType.isAssignableFrom(clz)
                && !targetType.equals(clz)
            ).collect(Collectors.toList());
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

    /**
     * 查找目标对象中的字段，并且用给定的值进行替换操作的方法。
     *
     * @param target    目标对象实例
     * @param fieldName 字段名称
     * @param value     新值
     * @throws IllegalAccessException 异常信息
     */
    public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
        Field field = FieldUtils.getField(target.getClass(), fieldName, true);
        if (field == null) {
            logger.warn("No field was found. Field name: [{}].", fieldName);

            return;
        }

        FieldUtils.writeField(field, target, value);
    }
}
