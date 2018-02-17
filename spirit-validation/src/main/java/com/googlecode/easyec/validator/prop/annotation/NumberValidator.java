package com.googlecode.easyec.validator.prop.annotation;

import com.googlecode.easyec.validator.prop.impl.NumberPropertyValidator;
import com.googlecode.easyec.validator.prop.impl.NumberPropertyValidator.Method;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Validator(NumberPropertyValidator.class)
public @interface NumberValidator {

    String message();

    Method method();

    double value() default 0;

    boolean i18n() default true;
}
