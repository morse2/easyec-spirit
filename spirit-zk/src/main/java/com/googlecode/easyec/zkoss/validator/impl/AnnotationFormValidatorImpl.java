package com.googlecode.easyec.zkoss.validator.impl;

import com.googlecode.easyec.validator.MessageCtrl;
import com.googlecode.easyec.validator.ValidationException;
import com.googlecode.easyec.validator.ValidationsException;
import com.googlecode.easyec.validator.prop.Property;
import com.googlecode.easyec.validator.prop.PropertyValidator;
import com.googlecode.easyec.validator.prop.annotation.*;
import com.googlecode.easyec.validator.prop.impl.NumberPropertyValidator.Method;
import com.googlecode.easyec.validator.prop.impl.PhonePropertyValidator;
import com.googlecode.easyec.zkoss.validator.AbstractFormValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.SaveBinding;
import org.zkoss.bind.sys.SavePropertyBinding;
import org.zkoss.xel.ValueReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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

                Object inst = valRefer.getBase();
                String name = (String) valRefer.getProperty();
                Field field = _findField(inst.getClass(), name);
                if (field == null) {
                    logger.warn("Field [{}] cannot be found in class [{}].", name, inst.getClass());

                    continue;
                }

                Annotation[] annotations = field.getAnnotations();
                if (ArrayUtils.isNotEmpty(annotations)) {
                    DelegateProperty delegate
                        = new DelegateProperty(
                        binding.getValidate(bindContext)
                    );

                    try {
                        for (Annotation ann : annotations) {
                            if (ann instanceof Validator) {
                                _validate(
                                    ((Validator) ann), delegate
                                );
                            }
                            // NullValidator
                            else if (ann instanceof NullValidator) {
                                _validate(
                                    ((NullValidator) ann), delegate
                                );
                            }
                            // NumberValidator
                            else if (ann instanceof NumberValidator) {
                                _validate(
                                    ((NumberValidator) ann), delegate
                                );
                            }
                            // EmailValidator
                            else if (ann instanceof EmailValidator) {
                                _validate(
                                    ((EmailValidator) ann), delegate
                                );
                            }
                            // PhoneValidator
                            else if (ann instanceof PhoneValidator) {
                                _validate(
                                    (PhoneValidator) ann, delegate
                                );
                            }
                            // check whether is custom validator
                            else {
                                Class<? extends Annotation> annType = ann.annotationType();
                                if (annType != null && annType.isAnnotationPresent(Validator.class)) {
                                    validateCustom(ann, delegate);
                                }
                            }
                        }
                    } catch (ValidationException e) {
                        e.setReference(binding.getComponent());

                        ex.add(e);
                    }
                }
            }

            if (ex.hasExceptions()) throw ex;
        }
    }

    private void _validate(NullValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        PropertyValidator pv = BeanUtils.instantiateClass(anno.value());
        _setMsgProps(pv, ann.message(), ann.localized());
        pv.validate(property);
    }

    private void _validate(NumberValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        PropertyValidator pv = BeanUtils.instantiateClass(
            getConstructorIfAvailable(anno.value(), Method.class, Number.class),
            ann.method(), ann.value()
        );

        _setMsgProps(pv, ann.message(), ann.localized());
        pv.validate(property);
    }

    private void _validate(EmailValidator ann, Property property) throws ValidationException {
        Validator anno = ann.annotationType().getAnnotation(Validator.class);
        PropertyValidator pv = BeanUtils.instantiateClass(anno.value());
        _setMsgProps(pv, ann.message(), ann.localized());
        pv.validate(property);
    }

    private void _validate(PhoneValidator ann, Property property) throws ValidationException {
        PhonePropertyValidator pv = BeanUtils.instantiateClass(ann.value());
        _setMsgProps(pv, ann.message(), ann.localized());
        pv.validate(property);
    }

    private void _validate(Validator ann, Property property) throws ValidationException {
        BeanUtils.instantiateClass(ann.value()).validate(property);
    }

    private Field _findField(Class<?> cls, String name) {
        if (cls == null) return null;

        Field field = ReflectionUtils.findField(cls, name);
        if (field != null) return field;

        return _findField(cls.getSuperclass(), name);
    }

    private void _setMsgProps(Object inst, String message, boolean localized) {
        if (inst instanceof MessageCtrl) {
            ((MessageCtrl) inst).setMessage(message);
            ((MessageCtrl) inst).setLocalized(localized);
        }
    }

    protected void validateCustom(Annotation ann, Property property) throws ValidationException {
        // no op
    }

    /* Default Property implementation */
    private class DelegateProperty implements Property {

        private org.zkoss.bind.Property _target;

        DelegateProperty(org.zkoss.bind.Property _target) {
            this._target = _target;
        }

        @Override
        public Object getBase() {
            return this._target.getBase();
        }

        @Override
        public Object getValue() {
            return this._target.getValue();
        }

        @Override
        public String getProperty() {
            return this._target.getProperty();
        }
    }
}
