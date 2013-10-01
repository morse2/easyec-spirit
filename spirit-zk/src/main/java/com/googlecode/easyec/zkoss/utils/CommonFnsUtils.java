package com.googlecode.easyec.zkoss.utils;

import org.apache.commons.lang.StringUtils;
import org.zkoss.xel.fn.CommonFns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * ZK通用功能类的扩展类。
 *
 * @author JunJie
 */
public class CommonFnsUtils {

    private static final Pattern P_UNICODE    = Pattern.compile("\\\\u[0-9A-Za-z]{4}");
    private static final String  DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取国际化标签的值。
     *
     * @param key 国际化标签的键
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key) {
        return parseUnicode(CommonFns.getLabel(key), key);
    }

    /**
     * 获取国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param args 格式化的参数值
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object[] args) {
        return parseUnicode(formatLabel(CommonFns.getLabel(key), args), key);
    }

    /**
     * 获取国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object frag) {
        return parseUnicode(CommonFns.getLabel(key + frag), key + frag);
    }

    /**
     * 获取国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @param args 格式化的参数值
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object frag, Object[] args) {
        return parseUnicode(formatLabel(CommonFns.getLabel(key + frag), args), key + frag);
    }

    /**
     * 格式化日期对象成字符串格式内容。
     *
     * @param date    日期对象
     * @param pattern 日期模式
     * @return 文本文字
     */
    public static String formatDate(Date date, String pattern) {
        if (null == date) return "";

        return
            new SimpleDateFormat(
                isBlank(pattern) ? DATE_PATTERN : pattern
            ).format(date);
    }

    /**
     * 用给定的参数，格式化标签值。
     *
     * @param label 标签值
     * @param args  参数列表，不能为空
     * @return 格式化后的标签值
     */
    private static String formatLabel(String label, Object[] args) {
        if (isBlank(label)) return null;
        if (isEmpty(args)) return label;

        String s = label;
        for (int i = 0; i < args.length; i++) {
            s = s.replaceAll("\\{" + i + "}", args[i].toString());
        }

        return s;
    }

    /**
     * 处理Unicode编码的方法。
     * 如果碰到中文的情况，ZK
     * 会转义乘Unicode编码，
     * 并输出。因此，此方法用来处理中文字符集。
     *
     * @param s   文字内容
     * @param key 查询的键
     * @return 非Unicode编码的文字内容
     */
    private static String parseUnicode(String s, String key) {
        StringBuffer sb = new StringBuffer();

        if (StringUtils.isNotBlank(s)) {
            Matcher m = P_UNICODE.matcher(s);
            while (m.find()) {
                m.appendReplacement(sb, valueOf((char) parseInt(m.group().replaceAll("\\\\u", ""), 16)));
            }

            m.appendTail(sb);
        } else {
            if (null == s) sb.append("???").append(key).append("???");
        }

        return sb.toString();
    }
}
