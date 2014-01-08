package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import com.googlecode.easyec.zkoss.zkjsp.databind.ReadonlyFieldTypeConverter;
import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author JunJie
 */
public class NumberFormatConverter extends ReadonlyFieldTypeConverter<Number, String> {

    public static final String DEFAULT_PATTERN = "#,##0.##";
    public static final String ARG_PATTERN     = "pattern";

    @Override
    public String coerceToUi(Number val, Map<String, Object> bindingArgs) {
        String pattern = (String) bindingArgs.get(ARG_PATTERN);
        if (StringUtils.isBlank(pattern)) pattern = DEFAULT_PATTERN;

        return new DecimalFormat(pattern).format(val);
    }
}
