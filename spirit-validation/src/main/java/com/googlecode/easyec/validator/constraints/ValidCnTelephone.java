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
    regexp = "^(0\\\\d{2}-\\\\d{8}(-\\\\d{1,4})?)|(0\\\\d{3}-\\\\d{7,8}(-\\\\d{1,4})?)$",
    flags = CASE_INSENSITIVE
)
public @interface ValidCnTelephone {

    String message() default "{spirit.validator.constraints.ValidCnTelephone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Documented
    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    public @interface List {

        ValidCnTelephone[] value();
    }
}
