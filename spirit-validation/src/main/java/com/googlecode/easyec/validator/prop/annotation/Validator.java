package com.googlecode.easyec.validator.prop.annotation;

import com.googlecode.easyec.validator.prop.PropertyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 支持字段验证的注解类
 *
 * @author junjie
 */
@Documented
@Retention(RUNTIME)
@Target({ ANNOTATION_TYPE, FIELD })
public @interface Validator {

    /**
     * 属性验证器的定义
     *
     * @return <code>PropertyValidator</code>
     */
    Class<? extends PropertyValidator> value();
}
