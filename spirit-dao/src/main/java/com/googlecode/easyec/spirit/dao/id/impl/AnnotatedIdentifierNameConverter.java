package com.googlecode.easyec.spirit.dao.id.impl;

import com.googlecode.easyec.spirit.dao.id.IdentifierNameConverter;
import com.googlecode.easyec.spirit.dao.id.IgnoreIdentifierGenerateException;
import com.googlecode.easyec.spirit.dao.id.annotation.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 注解形式的主键标识符名字解析器类。
 * 此类负责解析类级别上的标注了{@link Identifier}注解的类信息。
 * 如果当前类对象没有标注此注解类，那么则自动向上获得父类信息，并查找
 * 此注解类。以此类推，如果被解析的类，包括父类也没有标注此注解类，
 * 那么则返回null。但是在真正返回null之前，还可以通过注入
 * {@link ClassIdentifierNameConverter}类实例，来获取默认的主键标示名称。
 */
public class AnnotatedIdentifierNameConverter implements IdentifierNameConverter {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedIdentifierNameConverter.class);

    private static final ClassIdentifierNameConverter DEFAULT = new ClassIdentifierNameConverter();

    public String populate(Object obj) {
        Identifier id = findIdentifier(obj);
        if (null != id) {
            if (id.ignore()) {
                String msg = "Annotation 'Identifier' marked ignore to generate id.";
                logger.warn(msg);

                throw new IgnoreIdentifierGenerateException(msg);
            }

            Assert.hasText(id.value(), "No id name was set. class: [" + obj.getClass().getName() + "].");

            return id.value();
        }

        return DEFAULT.populate(obj);
    }

    private Identifier findIdentifier(Object o) {
        if (null == o) return null;

        Class<?> thisClass = o.getClass();

        // find annotation on this class.
        Identifier a = thisClass.getAnnotation(Identifier.class);
        if (null != a) return a;

        // find annotation on superclass
        Class<?> scl = thisClass.getSuperclass();
        while (null != scl) {
            a = scl.getAnnotation(Identifier.class);

            if (null != a) return a;

            scl = scl.getSuperclass();
        }

        return null;
    }
}
