package com.googlecode.easyec.zkoss.mvvm.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;

import java.util.Map;

/**
 * 表单验证基类
 *
 * @author JunJie
 */
public abstract class BaseFormValidator extends AbstractValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public final void validate(ValidationContext ctx) {
        try {
            validateForm(ctx);
        } catch (WrongValueException e) {
            logger.trace(e.getMessage(), e);

            setInvalid(ctx);

            throw e;
        } catch (WrongValuesException e) {
            logger.trace(e.getMessage(), e);

            setInvalid(ctx);

            throw e;
        }
    }

    /**
     * 验证表单的方法
     *
     * @param ctx 验证上下文对象
     */
    abstract protected void validateForm(ValidationContext ctx);

    /**
     * 得到当前表单的域模型对象
     *
     * @param ctx 验证上下文对象
     * @param <T> 泛型类型
     * @return 域模型对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T getDomainModel(ValidationContext ctx) {
        return (T) ctx.getProperty().getBase();
    }

    /**
     * 得到当前表单注入的属性集合
     *
     * @param ctx 验证上下文对象
     * @return 属性集合对象
     */
    protected Map<String, Property> getFormProperties(ValidationContext ctx) {
        return ctx.getProperties(getDomainModel(ctx));
    }

    /**
     * 得到当前表单的视图模型对象
     *
     * @param ctx 验证上下文对象
     * @param <T> 泛型类型
     * @return 视图模型对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T getViewModel(ValidationContext ctx) {
        return (T) ctx.getBindContext().getBinding().getBinder().getViewModel();
    }

    /**
     * 标记当前表单为无效
     *
     * @param ctx 验证上下文对象
     */
    protected void setInvalid(ValidationContext ctx) {
        if (ctx.isValid()) ctx.setInvalid();
    }
}
