package com.googlecode.easyec.validator.constraints.impl;

import com.googlecode.easyec.validator.constraints.ValidPassword;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.googlecode.easyec.validator.constraints.ValidPassword.Fields;
import static com.googlecode.easyec.validator.constraints.ValidPassword.Messages;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, Object> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ValidPassword anno;

    @Override
    public void initialize(ValidPassword anno) {
        this.anno = anno;
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext ctx) {
        Fields fields = anno.fields();
        Messages messages = anno.messages();

        BeanWrapperImpl bw = new BeanWrapperImpl(o);
        String password = getValue(bw, fields.password());
        String repeatPwd = getValue(bw, fields.repeatPassword());

        boolean b1 = false, b2 = false;
        // 校验密码是否为空
        if (isBlank(password)) {
            ctx.buildConstraintViolationWithTemplate(messages.emptyPassword())
                .addPropertyNode(fields.password())
                .addConstraintViolation();

            b1 = true;
        }

        if (isBlank(repeatPwd)) {
            ctx.buildConstraintViolationWithTemplate(messages.emptyRepeatPassword())
                .addPropertyNode(fields.repeatPassword())
                .addConstraintViolation();

            b2 = true;
        }

        if (b1 || b2) return false;

        if (!StringUtils.equals(password, repeatPwd)) {
            ctx.buildConstraintViolationWithTemplate(messages.passwordNotMatch())
                .addPropertyNode(fields.repeatPassword())
                .addConstraintViolation();

            return false;
        }

        return true;
    }

    private String getValue(BeanWrapper bw, String fieldName) {
        try {
            return (String) bw.getPropertyValue(fieldName);
        } catch (BeansException e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }
}
