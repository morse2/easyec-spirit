package com.googlecode.easyec.zkoss.validator.prop.annotation;

import com.googlecode.easyec.zkoss.validator.prop.impl.PhonePropertyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface PhoneValidator {

    /**
     * 电话号码属性验证器的定义
     *
     * @return <code>PhonePropertyValidator</code>
     */
    Class<? extends PhonePropertyValidator> value();

    String message();
}
