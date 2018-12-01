package com.googlecode.easyec.zkex.bind.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.*;

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
}
