package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java EL表达式字符串解析工具类。
 *
 * @author JunJie
 */
public class JexlUtils {

    private static final Logger logger = LoggerFactory.getLogger(JexlUtils.class);
    private static final Pattern p = Pattern.compile("\\$\\{[A-z]+}");

    /**
     * 解析EL表达式方法。
     *
     * @param elStr  表达式
     * @param params 表达式参数
     * @return 解析后的字符串内容
     */
    public static String parseEL(String elStr, Map<String, Object> params) {
        if (StringUtils.isBlank(elStr)) return null;

        StringBuffer sb = new StringBuffer();
        JexlEngine engine = new JexlEngine();

        Matcher m = p.matcher(elStr);
        while (m.find()) {
            String g = m.group();
            logger.debug("EL expression is: [" + g + "].");

            Object o = engine.createExpression(g.substring(2, g.length() - 1)).evaluate(new MapContext(params));
            logger.debug("Result of EL expression is: [" + o + "].");

            m.appendReplacement(sb, o.toString());
        }

        return m.appendTail(sb).toString();
    }
}
