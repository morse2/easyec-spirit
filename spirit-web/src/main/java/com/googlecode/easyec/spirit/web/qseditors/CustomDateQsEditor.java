package com.googlecode.easyec.spirit.web.qseditors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

/**
 * 日期对象URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomDateQsEditor extends AbstractQueryStringEditor {

    private static final long serialVersionUID = 5673856401904485955L;
    private String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public CustomDateQsEditor() { /* no op */ }

    public CustomDateQsEditor(String datePattern) {
        Assert.isTrue(isNotBlank(datePattern), "This parameter cannot be null.");

        this.datePattern = datePattern;
    }

    @Override
    protected String coerceOneObjectToQs(Object bean) {
        if (bean instanceof Date) {
            return format((Date) bean, datePattern, Locale.US);
        }

        if (bean instanceof Calendar) {
            return format((Calendar) bean, datePattern, Locale.US);
        }

        return null;
    }

    @Override
    protected Object coerceOneValueToBean(String qs) {
        try {
            return DateUtils.parseDate(qs, datePattern);
        } catch (ParseException e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }
}
