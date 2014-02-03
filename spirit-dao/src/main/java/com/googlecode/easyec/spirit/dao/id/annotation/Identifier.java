package com.googlecode.easyec.spirit.dao.id.annotation;

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
public @interface Identifier {

    /**
     * 主键名称。
     *
     * @return 当前PO的主键名称
     */
    String value() default "";

    /**
     * 标识当前PO对象的主键值不需要自动生成
     *
     * @return 默认为假，表示主键将被自动生成
     */
    boolean ignore() default false;
}
