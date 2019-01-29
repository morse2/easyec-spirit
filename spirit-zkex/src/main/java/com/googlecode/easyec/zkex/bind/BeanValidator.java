package com.googlecode.easyec.zkex.bind;

import com.googlecode.easyec.validator.support.ValidationContextHolder;
import com.googlecode.easyec.zkex.bind.utils.ValidationUtils;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Form;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.xel.zel.BindELContext;
import org.zkoss.xel.ExpressionX;
import org.zkoss.xel.ValueReference;
import org.zkoss.zk.ui.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.getBean;
import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.isInitialized;
import static com.googlecode.easyec.zkex.bind.utils.ValidationUtils.getFormObject;
import static org.zkoss.bind.sys.BinderCtrl.LOAD_FORM_COMPONENT;
import static org.zkoss.bind.sys.BinderCtrl.LOAD_FORM_EXPRESSION;

public class BeanValidator extends org.zkoss.bind.validator.BeanValidator {

    @Override
    protected Validator getValidator() {
        if (!isInitialized()) return super.getValidator();
        ValidatorFactory validatorFactory = getBean(ValidatorFactory.class);
        return validatorFactory != null
            ? validatorFactory.getValidator()
            : super.getValidator();
    }

    @Override
    public void validate(ValidationContext ctx) {
        final Property p = ctx.getProperty();
        final Object base = p.getBase();
        String property = p.getProperty();
        final Object value = p.getValue();

        Object[] info = getValidationInfo(ctx, base, property);
        Class<?> clz = (Class<?>) info[0];
        property = (String) info[1];
        final Class<?>[] groups = (Class<?>[]) ctx.getValidatorArg("groups");

        if (!shouldValidate(ctx)) {
            ValidationMessages vmsgs
                = ((BinderCtrl) ctx.getBindContext()
                .getBinder())
                .getValidationMessages();
            if (vmsgs != null) vmsgs.clearKeyMessages(property);

            return;
        }

        Set<ConstraintViolation<?>> violations = new HashSet<>();

        try {
            ValidationContextHolder.set(getFormObject(ctx), ctx.getCommand());
            violations.addAll(validate(clz, property, value, groups));
        } finally {
            ValidationContextHolder.remove();
        }

        handleConstraintViolation(ctx, violations);
    }

    @Override
    protected Object[] getValidationInfo(ValidationContext ctx, Object base, String property) {
        if (!(base instanceof Form)) {
            return super.getValidationInfo(ctx, base, property);
        }

        Component formComp = (Component) ctx.getBindContext().getAttribute(LOAD_FORM_COMPONENT);
        if (formComp == null) {
            throw new NullPointerException("form component not found");
        }

        String loadExpr = (String) formComp.getAttribute(LOAD_FORM_EXPRESSION);
        if (loadExpr == null) {
            throw new NullPointerException("load expression not found on the Form " + base
                + ", a bean validator doesn't support to validate a form that doesn't loads a bean yet");
        }

        String beanExpr = BindELContext.appendFields(loadExpr, property);
        Binder binder = ctx.getBindContext().getBinder();
        ExpressionX expr = binder.getEvaluatorX().parseExpressionX(null, beanExpr, Object.class);
        ValueReference vr = binder.getEvaluatorX().getValueReference(ctx.getBindContext(), formComp, expr);
        if (vr == null) {
            throw new NullPointerException("loaded instance not found on the Form " + base
                + ", a bean validator doesn't support to validate a form that doesn't loads a bean yet");
        }

        Class<?> clz = vr.getBase().getClass();
        property = vr.getProperty().toString();
        return new Object[] { clz, property };
    }

    protected boolean shouldValidate(String command, Object arg) {
        return ValidationUtils.shouldValidate(command, arg);
    }

    private boolean shouldValidate(ValidationContext ctx) {
        return shouldValidate(ctx.getCommand(), ctx.getValidatorArg("for"));
    }
}
