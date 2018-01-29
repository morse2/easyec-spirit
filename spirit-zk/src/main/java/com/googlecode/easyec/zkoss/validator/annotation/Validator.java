package com.googlecode.easyec.zkoss.validator.annotation;

import com.googlecode.easyec.zkoss.validator.PropertyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 支持字段验证的注解类
 *
 * @author junjie
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Validator {

    /**
     * 属性验证器的定义
     *
     * @return <code>PropertyValidator</code>
     */
    Class<? extends PropertyValidator> value();
}
