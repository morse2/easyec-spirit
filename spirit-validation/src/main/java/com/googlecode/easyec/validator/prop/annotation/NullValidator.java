package com.googlecode.easyec.validator.prop.annotation;

import com.googlecode.easyec.validator.prop.impl.NullPropertyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Validator(NullPropertyValidator.class)
public @interface NullValidator {

    String message();

    boolean i18n() default true;
}
