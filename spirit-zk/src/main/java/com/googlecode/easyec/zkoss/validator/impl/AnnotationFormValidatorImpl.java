package com.googlecode.easyec.zkoss.validator.impl;

import com.googlecode.easyec.zkoss.validator.AbstractFormValidator;
import com.googlecode.easyec.zkoss.validator.PropertyValidator;
import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.ValidationsException;
import com.googlecode.easyec.zkoss.validator.annotation.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.SaveBinding;
import org.zkoss.bind.sys.SavePropertyBinding;
import org.zkoss.xel.ValueReference;

import java.lang.reflect.Field;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 支持注解类{@link Validator}的表单验证的实现类
 *
 * @author junjie
 */
public class AnnotationFormValidatorImpl extends AbstractFormValidator {

    @Override
    protected void validateForm(ValidationContext ctx) throws ValidationsException {
        BindContext bindContext = ctx.getBindContext();

        Assert.isTrue(
            bindContext.getBinder() instanceof BinderCtrl,
            "AnnotationFormValidator must use AnnotationBinder."
        );

        // 获取当前表单的domainModel对象
        Object dm = ctx.getProperty().getBase();

        // 获取Binder对象实例
        Binder binder = bindContext.getBinder();
        Set<SaveBinding> bindings
            = ((BinderCtrl) binder)
            .getFormAssociatedSaveBindings(
                bindContext.getComponent()
            );

        if (isNotEmpty(bindings)) {
            BeanWrapper bw = createInstance(dm);

            ValidationsException ex = new ValidationsException();
            for (SaveBinding binding : bindings) {
                if (!(binding instanceof SavePropertyBinding)) {
                    logger.warn("SaveBinding must be instanceof SavePropertyBinding.");

                    continue;
                }

                ValueReference valRefer
                    = binder.getEvaluatorX()
                    .getValueReference(
                        bindContext,
                        binding.getComponent(),
                        ((SavePropertyBinding) binding).getProperty()
                    );

                String name = (String) valRefer.getProperty();
                Field field
                    = ReflectionUtils.findField(
                    bw.getWrappedClass(), name
                );

                if (field == null) {
                    logger.warn("Field [{}] cannot be found in class [{}].", name, bw.getWrappedClass());

                    continue;
                }

                if (!field.isAnnotationPresent(Validator.class)) {
                    logger.warn("No any annotation 'Validator' was present. Field name: [{}].", name);

                    continue;
                }

                Validator ann = field.getAnnotation(Validator.class);
                PropertyValidator va = BeanUtils.instantiateClass(ann.value());

                try {
                    va.validate(binding.getValidate(bindContext));
                } catch (ValidationException e) {
                    e.setComponent(binding.getComponent());

                    ex.add(e);
                }
            }

            if (ex.hasExceptions()) throw ex;
        }
    }

    protected BeanWrapper createInstance(Object dm) {
        return new BeanWrapperImpl(dm);
    }
}
