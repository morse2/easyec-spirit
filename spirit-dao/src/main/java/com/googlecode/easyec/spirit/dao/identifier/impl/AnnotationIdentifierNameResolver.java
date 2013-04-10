package com.googlecode.easyec.spirit.dao.identifier.impl;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierNameResolver;
import com.googlecode.easyec.spirit.dao.identifier.annotation.IdentifierName;
import org.springframework.util.Assert;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 注解形式的主键标识符名字解析器类。
 * 此类负责解析类级别上的标注了{@link IdentifierName}注解的类信息。
 * 如果当前类对象没有标注此注解类，那么则自动向上获得父类信息，并查找
 * 此注解类。以此类推，如果被解析的类，包括父类也没有标注此注解类，
 * 那么则返回null。但是在真正返回null之前，还可以通过注入
 * {@link DefaultIdentifierNameResolver}类实例，来获取默认的主键标示名称。
 *
 * @author JunJie
 * @see DefaultIdentifierNameResolver
 * @see IdentifierName
 */
public class AnnotationIdentifierNameResolver implements IdentifierNameResolver {

    private IdentifierNameResolver identifierNameResolver;

    /**
     * set方法。用于设置一个{@link IdentifierNameResolver}的实例。
     *
     * @param identifierNameResolver {@link IdentifierNameResolver}
     */
    public void setIdentifierNameResolver(IdentifierNameResolver identifierNameResolver) {
        this.identifierNameResolver = identifierNameResolver;
    }

    public String populate(Object o) {
        String name = findIdentifierName(o);

        if (isNotBlank(name)) return name;

        if (null != identifierNameResolver) {
            name = identifierNameResolver.populate(o);
        }

        Assert.isTrue(isNotBlank(name), "No any identifier name was resolved.");

        return name;
    }

    private String findIdentifierName(Object o) {
        if (null == o) return null;

        Class<?> thisClass = o.getClass();

        // find annotation on this class.
        IdentifierName a = thisClass.getAnnotation(IdentifierName.class);
        if (null != a) return a.value();

        // find annotation on superclass
        Class<?> scl = thisClass.getSuperclass();
        while (null != scl) {
            a = scl.getAnnotation(IdentifierName.class);

            if (null != a) return a.value();

            scl = scl.getSuperclass();
        }

        return null;
    }
}
