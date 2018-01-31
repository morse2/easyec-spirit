package com.googlecode.easyec.zkoss.validator.impl;

import com.googlecode.easyec.zkoss.validator.AbstractFormValidator;
import com.googlecode.easyec.zkoss.validator.ValidationException;
import com.googlecode.easyec.zkoss.validator.ValidationsException;
import com.googlecode.easyec.zkoss.validator.prop.PropertyValidator;
import com.googlecode.easyec.zkoss.validator.prop.annotation.EmailValidator;
import com.googlecode.easyec.zkoss.validator.prop.annotation.NullValidator;
import com.googlecode.easyec.zkoss.validator.prop.annotation.NumberValidator;
import com.googlecode.easyec.zkoss.validator.prop.annotation.Validator;
import com.googlecode.easyec.zkoss.validator.prop.impl.NumberPropertyValidator.Method;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.SaveBinding;
import org.zkoss.bind.sys.SavePropertyBinding;
import org.zkoss.xel.ValueReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.aop.support.AopUtils.getTargetClass;
import static org.springframework.util.ClassUtils.getConstructorIfAvailable;

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

        // 获取Binder对象实例
        Binder binder = bindContext.getBinder();
        Set<SaveBinding> bindings
            = ((BinderCtrl) binder)
            .getFormAssociatedSaveBindings(
                bindContext.getComponent()
            );

        if (isNotEmpty(bindings)) {
            ConcurrentMap<Class<?>, BeanWrapper> beanMap = new ConcurrentHashMap<>();
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

                Class<?> _curClz = getTargetClass(valRefer.getBase());
                BeanWrapper bw = beanMap.get(_curClz);
                if (bw == null) {
                    bw = createInstance(_curClz);
                    beanMap.put(_curClz, bw);
                }

                String name = (String) valRefer.getProperty();
                Field field
                    = ReflectionUtils.findField(
                    bw.getWrappedClass(), name
                );

                if (field == null) {
                    logger.warn("Field [{}] cannot be found in class [{}].", name, bw.getWrappedClass());

                    continue;
                }

                Annotation[] annotations = field.getAnnotations();
                if (ArrayUtils.isNotEmpty(annotations)) {
                    try {
                        for (Annotation ann : annotations) {
                            if (ann instanceof Validator) {
                                _validate(
                                    ((Validator) ann),
                                    binding.getValidate(bindContext)
                                );
                            }
                            // NullValidator
                            else if (ann instanceof NullValidator) {
                                _validate(
                                    ((NullValidator) ann),
                                    binding.getValidate(bindContext)
                                );
                            }
                            // NumberValidator
                            else if (ann instanceof NumberValidator) {
                                _validate(
                                    ((NumberValidator) ann),
                                    binding.getValidate(bindContext)
                                );
                            }
                            // EmailValidator
                            else if (ann instanceof EmailValidator) {
                                _validate(
                                    ((EmailValidator) ann),
                                    binding.getValidate(bindContext)
                                );
                            }
                            // check whether is custom validator
                            else {
                                Class<? extends Annotation> annType = ann.annotationType();
                                if (annType != null && annType.isAnnotationPresent(Validator.class)) {
                                    validateCustom(ann, binding.getValidate(bindContext));
                                }
                            }
                        }
                    } catch (ValidationException e) {
                        e.setComponent(binding.getComponent());

                        ex.add(e);
                    }
                }
            }

            if (ex.hasExceptions()) throw ex;
        }
    }

    private void _validate(NullValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        validate(getConstructorIfAvailable(anno.value(), String.class), property, ann.message());
    }

    private void _validate(NumberValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        validate(getConstructorIfAvailable(anno.value(), String.class, Method.class, Number.class),
            property, ann.message(), ann.method(), ann.value());
    }

    private void _validate(EmailValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        validate(getConstructorIfAvailable(anno.value(), String.class), property, ann.message());
    }

    private void _validate(Validator ann, Property property) throws ValidationException {
        validate(getConstructorIfAvailable(ann.value()), property);
    }

    protected BeanWrapper createInstance(Object dm) {
        return new BeanWrapperImpl(dm);
    }

    protected void validate(Constructor<? extends PropertyValidator> cons, Property property, Object... values) throws ValidationException {
        BeanUtils.instantiateClass(cons, values).validate(property);
    }

    protected void validateCustom(Annotation ann, Property property) throws ValidationException {
        // no op
    }
}
