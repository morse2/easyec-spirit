package com.googlecode.easyec.spirit.web.qseditors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.time.DateFormatUtils.format;
import static org.apache.commons.lang.time.DateUtils.parseDate;

/**
 * 日期对象URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomDateQsEditor implements QueryStringEditor {

    private static final Logger logger = LoggerFactory.getLogger(CustomDateQsEditor.class);
    private static final long serialVersionUID = -4032969280496161035L;

    private String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public CustomDateQsEditor() { /* no op */ }

    public CustomDateQsEditor(String datePattern) {
        Assert.isTrue(isNotBlank(datePattern), "This parameter cannot be null.");

        this.datePattern = datePattern;
    }

    public boolean accept(Object bean) {
        return bean != null;
    }

    public String coerceToQs(Object bean) {
        if (bean instanceof Date) {
            return format((Date) bean, datePattern, Locale.US);
        }

        if (bean instanceof Calendar) {
            return format((Calendar) bean, datePattern, Locale.US);
        }

        return "";
    }

    public Object coerceToBean(String qs) {
        if (isBlank(qs)) return null;

        try {
            return parseDate(
                qs, new String[] { datePattern }
            );
        } catch (ParseException e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }
}
