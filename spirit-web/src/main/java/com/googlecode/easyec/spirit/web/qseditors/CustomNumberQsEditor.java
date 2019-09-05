package com.googlecode.easyec.spirit.web.qseditors;

import org.springframework.util.NumberUtils;

/**
 * 数字类型的URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomNumberQsEditor extends AbstractQueryStringEditor {

    private static final long serialVersionUID = 5199246234369338208L;
    private Class<? extends Number> numberClass;

    public CustomNumberQsEditor(Class<? extends Number> numberClass) {
        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }

        this.numberClass = numberClass;
    }

    @Override
    protected String coerceOneObjectToQs(Object bean) {
        return bean.toString();
    }

    @Override
    protected Object coerceOneValueToBean(String qs) {
        try {
            return NumberUtils.parseNumber(qs, numberClass);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }
}
