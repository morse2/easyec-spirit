package com.googlecode.easyec.spirit.web.qseditors;

/**
 * 字符串类型的URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomStringQsEditor implements QueryStringEditor {

    private static final long serialVersionUID = -4928561627500461546L;

    public boolean accept(Object bean) {
        return bean != null;
    }

    public String coerceToQs(Object bean) {
        if (bean instanceof String) {
            return (String) bean;
        }

        return bean.toString();
    }

    public Object coerceToBean(String qs) {
        return qs;
    }
}
