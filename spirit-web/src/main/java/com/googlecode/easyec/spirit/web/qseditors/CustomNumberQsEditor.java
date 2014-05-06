package com.googlecode.easyec.spirit.web.qseditors;

import org.springframework.util.NumberUtils;

/**
 * 数字类型的URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomNumberQsEditor implements QueryStringEditor {

    private static final long serialVersionUID = 1070118845998707272L;
    private Class<? extends Number> numberClass;

    public CustomNumberQsEditor(Class<? extends Number> numberClass) {
        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }

        this.numberClass = numberClass;
    }

    public boolean accept(Object bean) {
        return bean != null;
    }

    public String coerceToQs(Object bean) {
        return bean.toString();
    }

    public Object coerceToBean(String qs) {
        return NumberUtils.parseNumber(qs, numberClass);
    }
}
