package com.googlecode.easyec.validator.constraints;

import com.googlecode.easyec.validator.constraints.impl.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface ValidPassword {

    Fields fields();

    Messages messages();

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    public @interface Fields {

        String password() default "password";

        String repeatPassword() default "repeatPassword";
    }

    public @interface Messages {

        String emptyPassword() default "{spirit.validator.constraints.ValidPassword.emptyPassword}";

        String emptyRepeatPassword() default "{spirit.validator.constraints.ValidPassword.emptyRepeatPassword}";

        String passwordNotMatch() default "{spirit.validator.constraints.ValidPassword.passwordNotMatch}";
    }
}
