package com.googlecode.easyec.zkex.bind.utils;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.ValidationContext;

/**
 * ZK验证的工具类
 *
 * @author junjie
 */
public class ValidationUtils {

    private ValidationUtils() {}

    /**
     * 表示方法是否需要执行表单验证的操作
     *
     * @param ctx <code>ValidationContext</code>
     * @return bool
     */
    public static boolean shouldValidate(ValidationContext ctx) {
        Object _for = ctx.getValidatorArg("for");
        if (_for != null) {
            if (_for instanceof String) {
                return StringUtils.equals(
                    ctx.getCommand(), (String) _for
                );
            }

            if (_for instanceof String[]) {
                return StringUtils.equalsAny(
                    ctx.getCommand(), ((String[]) _for)
                );
            }
        }

        return true;
    }
}
