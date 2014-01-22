package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import com.googlecode.easyec.zkoss.zkjsp.databind.ReadonlyFieldTypeConverter;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * 日期类型的字段格式转换类
 *
 * @author JunJie
 */
public class DateFormatConverter extends ReadonlyFieldTypeConverter<Date, String> {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static final String ARG_PATTERN     = "pattern";

    @Override
    public String coerceToUi(Date val, Map<String, Object> bindingArgs) {
        if (null == val) return "";

        String pattern = (String) bindingArgs.get(ARG_PATTERN);
        if (isBlank(pattern)) pattern = DEFAULT_PATTERN;

        return DateFormatUtils.format(val, pattern);
    }
}
