package com.googlecode.easyec.validator.constraints;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@ReportAsSingleViolation
@NotBlank
@Constraint(validatedBy = {})
@ValidRegexp(
    regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$",
    flags = CASE_INSENSITIVE
)
public @interface ValidCnCellphone {

    String message() default "{spirit.validator.constraints.ValidCnCellphone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    public @interface List {

        ValidCnCellphone[] value();
    }
}
