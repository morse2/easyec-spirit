package com.googlecode.easyec.zkex.bind;

import com.googlecode.easyec.validator.support.ValidationContextHolder;
import com.googlecode.easyec.zkex.bind.utils.ValidationUtils;
import com.googlecode.easyec.zkoss.viewmodel.FormViewModelAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.SaveFormBinding;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.bind.validator.BeanValidations;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.UiException;

import javax.validation.ConstraintViolation;
import javax.validation.GroupSequence;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.getBean;
import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.isInitialized;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;

public class FormBeanValidator extends AbstractValidator {

    private static final Logger logger = LoggerFactory.getLogger(FormBeanValidator.class);

    protected Validator getValidator() {
        if (!isInitialized()) return BeanValidations.getValidator();
        ValidatorFactory validatorFactory = getBean(ValidatorFactory.class);
        return validatorFactory != null
            ? validatorFactory.getValidator()
            : BeanValidations.getValidator();
    }

    protected void handleConstraintViolation(ValidationContext ctx, String key, Set<ConstraintViolation<?>> violations) {
        if (size(violations) == 1) {
            this.addInvalidMessages(ctx, key, new String[] { violations.iterator().next().getMessage() });
        } else if (size(violations) > 0) {
            String[] msgs
                = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toArray(String[]::new);

            this.addInvalidMessages(ctx, key, msgs);
        }
    }

    protected Class getBeanClass(ValidationContext ctx, Object base) {
        return base.getClass();
    }

    public void validate(ValidationContext ctx) {
        if (!(ctx.getBindContext().getBinding() instanceof SaveFormBinding)) {
            throw new UiException("Can be used in form binding only");
        }

        String prefix = (String) ctx.getValidatorArg("prefix");
        if (Strings.isEmpty(prefix)) {
            throw new NullPointerException("prefix of message key is empty, did you set prefix argument in @validator?");
        }

        if (!shouldValidate(ctx)) {
            ValidationMessages vmsgs
                = ((BinderCtrl) ctx.getBindContext()
                .getBinder())
                .getValidationMessages();
            if (vmsgs != null) vmsgs.clearAllMessages();

            return;
        }

        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        // get FormObject from context
        Object formObject = ValidationUtils.getFormObject(ctx);

        try {
            // set Form bean into current thread.
            ValidationContextHolder.set(formObject, ctx.getCommand());
            Class<?>[] groups = getGroups(ctx);
            if (isEmpty(groups)) {
                violations.addAll(
                    getValidator().validate(formObject)
                );
            } else {
                Validator validator = getValidator();
                for (Class<?> clz : groups) {
                    if (!clz.isInterface()) {
                        logger.warn("Class isn't an interface. [" + clz.getName() + "]");

                        continue;
                    }

                    violations.addAll(
                        validator.validate(formObject, clz)
                    );

                    if (isNotEmpty(violations)) break;
                }
            }
        } finally {
            ValidationContextHolder.remove();
        }

        if (violations.size() > 0) {
            Map<String, Set<ConstraintViolation<?>>> vMap = new HashMap<>();
            for (ConstraintViolation<?> violation : violations) {
                String path = violation.getPropertyPath().toString();
                vMap.computeIfAbsent(path, k -> new HashSet<>()).add(violation);
            }

            vMap.forEach((k, v) ->
                handleConstraintViolation(ctx, prefix + k, v)
            );
        }
    }

    protected boolean shouldValidate(String command, Object arg) {
        return ValidationUtils.shouldValidate(command, arg);
    }

    private boolean shouldValidate(ValidationContext ctx) {
        return shouldValidate(ctx.getCommand(), ctx.getValidatorArg("for"));
    }

    private Class<?>[] getGroups(ValidationContext ctx) {
        Class<?>[] groups = (Class<?>[]) ctx.getValidatorArg("groups");
        if (isEmpty(groups)) {
            Object vm = ctx.getBindContext().getBinder().getViewModel();
            if (vm instanceof FormViewModelAware) {
                groups = ((FormViewModelAware) vm).getFormBeanValidatorGroups();
            }
        }

        if (isEmpty(groups)) {
            Object base = ctx.getProperty().getBase();
            Class cls = getBeanClass(ctx, base);
            Annotation anno = cls.getAnnotation(GroupSequence.class);
            if (anno != null) groups = ((GroupSequence) anno).value();
        }

        return nullToEmpty(groups);
    }
}
