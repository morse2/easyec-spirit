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
@Deprecated
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
    @Deprecated
    protected Map<String, Property> getFormProperties(ValidationContext ctx) {
        return ctx.getProperties((Object) getDomainModel(ctx));
    }

    /**
     * 得到当前表单注入的对象的属性集合
     *
     * @param ctx     验证上下文对象
     * @param formObj 表单对象
     * @return 对象属性集合
     */
    protected Map<String, Property> getFormProperties(ValidationContext ctx, Object formObj) {
        return ctx.getProperties(formObj);
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
     * 得到当前表单对象实例。
     * 该表单对象实例可以是一个表单代理类，
     * 也可以是非代理对象。但实际上返回的对象
     * 类型应该与{@link #getDomainModel(ValidationContext)}
     * 方法返回的类型一致
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

    /**
     * 标记当前表单为无效
     *
     * @param ctx 验证上下文对象
     */
    protected void setInvalid(ValidationContext ctx) {
        if (ctx.isValid()) ctx.setInvalid();
    }
}
