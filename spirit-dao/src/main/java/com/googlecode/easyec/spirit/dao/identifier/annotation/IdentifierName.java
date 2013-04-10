package com.googlecode.easyec.spirit.dao.identifier.annotation;

import java.lang.annotation.*;

/**
 * 主键标识的注解类。
 * 此类用于设置PO对象的主键名称。
 *
 * @author JunJie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentifierName {

    /**
     * 主键名称。
     *
     * @return 当前PO的主键名称
     */
    String value();
}
