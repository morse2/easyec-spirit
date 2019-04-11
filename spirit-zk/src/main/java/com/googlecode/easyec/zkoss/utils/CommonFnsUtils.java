package com.googlecode.easyec.zkoss.utils;

import com.googlecode.easyec.spirit.web.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.xel.fn.CommonFns;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.http.WebManager;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.*;

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
        return parseUnicode(formatLabel(CommonFns.getLabel(key), args), key);
    }

    /**
     * 获取国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @return 国际化标签键对应的值
     */
    public static String getLabelWithFrag(String key, Object frag) {
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
    public static String getLabelWithFrag(String key, Object frag, Object[] args) {
        return parseUnicode(formatLabel(CommonFns.getLabel(key + frag), args), key + frag);
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @param arg1 参数
     * @return 国际化标签键对应的值
     */
    public static String getLabelWithFrag(String key, Object frag, Object arg1) {
        return getLabelWithFrag(key, frag, new Object[] { arg1 });
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 国际化标签键对应的值
     */
    public static String getLabelWithFrag(String key, Object frag, Object arg1, Object arg2) {
        return getLabelWithFrag(key, frag, new Object[] { arg1, arg2 });
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param frag 国际化标签的键的片段值
     * @param arg1 参数1
     * @param arg2 参数2
     * @param arg3 参数3
     * @return 国际化标签键对应的值
     */
    public static String getLabelWithFrag(String key, Object frag, Object arg1, Object arg2, Object arg3) {
        return getLabelWithFrag(key, frag, new Object[] { arg1, arg2, arg3 });
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param arg1 参数
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object arg1) {
        return getLabel(key, new Object[] { arg1 });
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object arg1, Object arg2) {
        return getLabel(key, new Object[] { arg1, arg2 });
    }

    /**
     * 获取带参数的国际化标签的值。
     *
     * @param key  国际化标签的键
     * @param arg1 参数1
     * @param arg2 参数2
     * @param arg3 参数3
     * @return 国际化标签键对应的值
     */
    public static String getLabel(String key, Object arg1, Object arg2, Object arg3) {
        return getLabel(key, new Object[] { arg1, arg2, arg3 });
    }

    /**
     * 格式化日期对象成字符串格式内容。
     *
     * @param date    日期对象
     * @param pattern 日期模式
     * @return 文本文字
     */
    public static String formatDate(Date date, String pattern) {
        return null != date ? DateFormatUtils.format(date, pattern) : "";
    }

    /**
     * 格式化给定的日期对象，
     * 如果日期对象不是空，那么
     * 就格式化成yyyy-MM-dd字符串。
     *
     * @param date <code>java.util.Date</code>
     * @return 格式化后的日期字符串
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 格式化给定的日期对象，
     * 如果日期对象不是空，那么
     * 就格式化成yyyy-MM-dd HH:mm字符串。
     *
     * @param date <code>java.util.Date</code>
     * @return 格式化后的日期字符串
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 获得给定资源对应的ZK的资源路径
     *
     * @param uri URI
     * @return ZK URI
     */
    public static String getWebResourceUri(String uri) {
        if (isBlank(uri)) return EMPTY;

        WebApp webApp = Sessions.getCurrent().getWebApp();
        String ctxPath = webApp.getServletContext().getContextPath();
        if (startsWith(uri, "/")) {
            return WebUtils.appendCtx(ctxPath, uri);
        }

        if (startsWith(uri, "~./")) {
            WebManager wm = WebManager.getWebManager(webApp);
            if (wm == null) return uri;

            ClassWebResource cwr = wm.getClassWebResource();
            return WebUtils.appendCtx(ctxPath,
                new StringBuffer()
                    .append(wm.getUpdateURI())
                    .append(ClassWebResource.PATH_PREFIX)
                    .append(cwr.getEncodeURLPrefix())
                    .append(uri.substring(2))
                    .toString()
            );
        }

        return uri;
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
            Object arg = args[i];
            if (arg == null) arg = "null";
            s = s.replaceAll("\\{" + i + "}", arg.toString());
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
