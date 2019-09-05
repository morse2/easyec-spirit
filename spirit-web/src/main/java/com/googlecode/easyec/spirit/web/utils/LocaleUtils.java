package com.googlecode.easyec.spirit.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * 本地化工具类
 *
 * @author JunJie
 */
public class LocaleUtils {

    private LocaleUtils() { /* no op */ }

    /**
     * 返回得到本地化查询字符串
     *
     * @param pageContext 页面上下文对象
     * @param locale      本地化字符代码
     * @return 查询字符串
     */
    public static String getLocaleQs(PageContext pageContext, String locale) {
        Map<String, String[]> map = WebUtils.decodeQueryString(
            (HttpServletRequest) pageContext.getRequest()
        );

        map.put("locale", new String[] { locale });

        return WebUtils.encodeQueryString(map);
    }
}
