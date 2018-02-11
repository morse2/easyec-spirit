package com.googlecode.easyec.zkoss.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.equalsAny;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractFormValidator extends AbstractValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void validate(ValidationContext ctx) {
        if (!shouldBeValidated(ctx.getCommand())) {
            logger.debug("The command [{}] shouldn't do validation.", ctx.getCommand());

            return;
        }

        try {
            // begin validating
            validateForm(ctx);
        } catch (WrongValueException e) {
            if (!ctx.isValid()) ctx.setInvalid();

            handleException(
                new WrongValuesException(
                    new WrongValueException[] { e }
                )
            );
        } catch (WrongValuesException e) {
            if (!ctx.isValid()) ctx.setInvalid();

            handleException(e);
        } catch (ValidationsException e) {
            if (!ctx.isValid()) ctx.setInvalid();

            handleException(e);
        }
    }

    /**
     * 执行表单验证的抽象的方法
     *
     * @param ctx <code>ValidationContext</code>
     * @throws ValidationsException 验证失败的异常信息
     */
    abstract protected void validateForm(ValidationContext ctx) throws ValidationsException;

    /**
     * 判断给定的command是否需要执行验证操作的方法
     *
     * @param command 命令
     * @return 布尔值
     */
    protected boolean shouldBeValidated(String command) {
        return isNotBlank(command) && equalsAny(command, "submit", "save");
    }

    protected void handleException(ValidationsException e) {
        List<WrongValueException> result = new ArrayList<>();
        List<ValidationException> exceptions = e.getExceptions();
        for (ValidationException ex : exceptions) {
            WrongValueException ve = convertException(ex);
            if (ve != null) result.add(ve);
        }

        if (!result.isEmpty()) {
            handleException(
                new WrongValuesException(
                    result.toArray(new WrongValueException[result.size()])
                )
            );
        }
    }

    /**
     * 处理校验异常的方法
     *
     * @param e <code>WrongValuesException</code>
     */
    protected void handleException(WrongValuesException e) {
        if (e != null) {
            if (logger.isErrorEnabled()) {
                WrongValueException[] exceptions = e.getWrongValueExceptions();
                for (WrongValueException ex : exceptions) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            throw e;
        }
    }

    /**
     * 转换异常的方法
     *
     * @param e 源异常对象
     * @return 目标异常对象
     */
    protected WrongValueException convertException(ValidationException e) {
        return new WrongValueException(
            e.getComponent(),
            e.getMessage()
        );
    }

    /**
     * 得到当前表单的视图模型对象
     *
     * @param bindContext 绑定上下文
     * @param <T>         泛型类型
     * @return 视图模型对象
     */
    @SuppressWarnings("unchecked")
    protected <T> T getViewModel(BindContext bindContext) {
        return (T) bindContext.getBinder().getViewModel();
    }

    @SuppressWarnings("unchecked")
    protected <T> T getForm(ValidationContext ctx) {
        return (T) ctx.getProperty().getValue();
    }
}
