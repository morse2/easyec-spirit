package com.googlecode.easyec.zkex.bind.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.zk.ui.Component;

import java.util.Arrays;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.*;
import static org.zkoss.bind.sys.BinderCtrl.FORM_ID;

/**
 * ZK验证的工具类
 *
 * @author junjie
 */
public class ValidationUtils {

    private static final String SINGLE_QUOTATION_MARK = "\'";
    private static final String DOUBLE_QUOTATION_MARK = "\"";
    private static final String QUOTATION_MARKS = "\'\"";

    private ValidationUtils() {}

    public static boolean shouldValidate(String command, Object arg) {
        return (arg instanceof String)
            ? shouldValidate(command, ((String) arg))
            : (!(arg instanceof String[]))
            || shouldValidate(command, ((String[]) arg));
    }

    /**
     * 判断命令和给定的参数是否匹配，
     * 如果参数为空，或与命令匹配，
     * 则执行验证。
     *
     * @param command 命令
     * @param arg     验证参数
     * @return 是否验证的结果
     */
    public static boolean shouldValidate(String command, String arg) {
        return isBlank(arg) || StringUtils.equals(command, arg);
    }

    /**
     * 判断命令是否需要进行验证，
     * 如果与给的参数args中的任一
     * 值匹配，则进行验证。但如果
     * 参数args没有值，则默认
     * 进行验证。
     *
     * @param command 命令
     * @param args    验证参数
     * @return 是否验证的结果
     */
    public static boolean shouldValidate(String command, String[] args) {
        return ArrayUtils.isEmpty(args)
            || Arrays.stream(args)
            .map(s -> {
                boolean b = startsWithAny(s, SINGLE_QUOTATION_MARK, DOUBLE_QUOTATION_MARK)
                    && endsWithAny(s, SINGLE_QUOTATION_MARK, DOUBLE_QUOTATION_MARK);
                return b ? stripEnd(stripStart(s, QUOTATION_MARKS), QUOTATION_MARKS) : s;
            }).anyMatch(s -> StringUtils.equals(command, s));
    }

    /**
     * 得到当前表单对象实例。
     * 该表单对象实例可以是一个表单代理类，
     * 也可以是非代理对象。
     *
     * @param ctx ZK验证上下文对象
     * @return 表单对象实例
     */
    public static Object getFormObject(ValidationContext ctx) {
        Map<String, Property> formProperties
            = ctx.getProperties(
            ctx.getProperty().getBase()
        );

        if (formProperties != null && formProperties.containsKey(".")) {
            return formProperties.get(".").getValue();
        }

        Map<String, Property[]> allProperties = ctx.getProperties();
        if (allProperties != null && allProperties.containsKey(".")) {
            return allProperties.get(".")[0].getValue();
        }

        Component _root = ctx.getBindContext().getBinder().getView();
        String _formId = (String) _root.getAttribute(FORM_ID);
        if (isNotBlank(_formId)) return _root.getAttribute(_formId);

        return null;
    }
}
