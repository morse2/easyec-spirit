package com.googlecode.easyec.zkoss.validator.prop.annotation;

import com.googlecode.easyec.zkoss.validator.prop.impl.NullValuePropertyValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Validator(NullValuePropertyValidator.class)
public @interface NullValidator {

    String message();
}
