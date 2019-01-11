package com.googlecode.easyec.zkex.bind;

import com.googlecode.easyec.validator.support.ValidationContextHolder;
import com.googlecode.easyec.zkex.bind.utils.ValidationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.Property;
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
import java.util.stream.Stream;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.getBean;
import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.isInitialized;

public class FormBeanValidator extends AbstractValidator {

    protected Validator getValidator() {
        if (!isInitialized()) return BeanValidations.getValidator();
        ValidatorFactory validatorFactory = getBean(ValidatorFactory.class);
        return validatorFactory != null
            ? validatorFactory.getValidator()
            : BeanValidations.getValidator();
    }

    protected void handleConstraintViolation(ValidationContext ctx, String key, Set<ConstraintViolation<?>> violations) {
        if (CollectionUtils.size(violations) == 1) {
            this.addInvalidMessages(ctx, key, new String[] { violations.iterator().next().getMessage() });
        } else if (CollectionUtils.size(violations) > 0) {
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

        Object base = ctx.getProperty().getBase();
        Class cls = getBeanClass(ctx, base);
        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        // get FormObject from context
        Map<String, Property> beanProps = ctx.getProperties(base);
        Object formObject = getFormObject(beanProps);

        try {
            // set Form bean into current thread.
            ValidationContextHolder.set(formObject);
            // find GroupSequence annotation
            Annotation anno = cls.getAnnotation(GroupSequence.class);
            if (anno == null) {
                violations.addAll(
                    getValidator().validate(formObject)
                );
            } else {
                Class<?>[] value = ((GroupSequence) anno).value();
                if (value.length == 0) {
                    violations.addAll(
                        getValidator().validate(formObject)
                    );
                } else {
                    Validator validator = getValidator();
                    Stream.of(value).forEach(clz -> {
                        if (clz.isInterface()) {
                            violations.addAll(
                                validator.validate(formObject, clz)
                            );
                        } else violations.addAll(
                            getValidator().validate(formObject)
                        );
                    });
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
        if (arg instanceof String) {
            return ValidationUtils.shouldValidate(command, ((String) arg));
        }
        if (arg instanceof String[]) {
            return ValidationUtils.shouldValidate(command, ((String[]) arg));
        }

        return true;
    }

    /**
     * 得到当前表单对象实例。
     * 该表单对象实例可以是一个表单代理类，
     * 也可以是非代理对象。
     *
     * @param formProperties 表单属性对象
     * @return 表单对象实例
     */
    protected Object getFormObject(Map<String, Property> formProperties) {
        if (formProperties != null && formProperties.containsKey(".")) {
            return formProperties.get(".").getValue();
        }

        return null;
    }

    private boolean shouldValidate(ValidationContext ctx) {
        return shouldValidate(ctx.getCommand(), ctx.getValidatorArg("for"));
    }
}
