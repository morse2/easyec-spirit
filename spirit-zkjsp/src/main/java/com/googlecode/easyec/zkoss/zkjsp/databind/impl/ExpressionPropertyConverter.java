package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import com.googlecode.easyec.zkoss.zkjsp.databind.ReadonlyFieldTypeConverter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表示式型的对象属性解析的转换类
 *
 * @author JunJie
 */
public class ExpressionPropertyConverter extends ReadonlyFieldTypeConverter<Object, String> {

    private static final Logger  logger = LoggerFactory.getLogger(ExpressionPropertyConverter.class);
    private static final Pattern _p     = Pattern.compile("@\\{[A-z0-9\\.]+}");

    public static final String EXPRESSION_PATH = "expression";

    @Override
    public String coerceToUi(Object val, Map<String, Object> bindingArgs) {
        String value = (String) bindingArgs.get(EXPRESSION_PATH);
        if (StringUtils.isBlank(value)) {
            logger.debug("No value was set within binding args.");

            return null != val ? val.toString() : null;
        }

        try {
            return format(value, val);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    protected String format(String value, Object obj) throws Exception {
        logger.debug("Original value is: [{}].", value);

        StringBuffer sb = new StringBuffer();
        Matcher m = _p.matcher(value);
        while (m.find()) {
            String g = m.group();
            logger.debug("EL expression is: [" + g + "].");

            Object o = PropertyUtils.getProperty(obj, g.substring(2, g.length() - 1));
            logger.debug("Result of expression is: [" + o + "].");

            if (null == o) o = "";
            m.appendReplacement(sb, o.toString());
        }

        return m.appendTail(sb).toString();
    }
}
