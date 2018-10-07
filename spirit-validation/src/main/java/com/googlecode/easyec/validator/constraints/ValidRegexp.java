package com.googlecode.easyec.validator.constraints;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 有效的正则表达式的注解类
 *
 * @author junjie
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@ReportAsSingleViolation
@Pattern(regexp = "")
@Constraint(validatedBy = {})
public @interface ValidRegexp {

    String message() default "{spirit.validator.constraints.ValidRegexp.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @OverridesAttribute(constraint = Pattern.class, name = "regexp")
    String regexp() default ".*";

    @OverridesAttribute(constraint = Pattern.class, name = "flags")
    Pattern.Flag[] flags() default {};

    @Documented
    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    public @interface List {

        ValidRegexp[] value();
    }
}
