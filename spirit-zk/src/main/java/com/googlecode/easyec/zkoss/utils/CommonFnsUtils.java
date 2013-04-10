package com.googlecode.easyec.zkoss.utils;

import org.apache.commons.lang.StringUtils;
import org.zkoss.xel.fn.CommonFns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

/**
 * ZK通用功能类的扩展类。
 *
 * @author JunJie
 */
public class CommonFnsUtils {

    private static final Pattern P_UNICODE = Pattern.compile("\\\\u[0-9A-Za-z]{4}");

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
        return parseUnicode(CommonFns.getLabel(key, args), key);
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
        return parseUnicode(CommonFns.getLabel(key + frag, args), key + frag);
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
