package com.googlecode.easyec.zkex.bind;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.SaveFormBinding;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.bind.validator.BeanValidations;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.UiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;

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

    @SuppressWarnings("unchecked")
    protected Set<ConstraintViolation<?>> validate(Class clz, String property, Object value) {
        return this.getValidator().validateValue(clz, property, value);
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

        Object base = ctx.getProperty().getBase();
        Class<?> clz = getBeanClass(ctx, base);
        Map<String, Property> beanProps = ctx.getProperties(base);
        beanProps.values().forEach(prop -> {
            String pName = prop.getProperty();
            if (!".".equals(pName)) {
                Set<ConstraintViolation<?>> ret = validate(clz, pName, prop.getValue());
                if (CollectionUtils.size(ret) > 0) {
                    handleConstraintViolation(ctx, prefix + pName, ret);
                }
            }
        });
    }
}
