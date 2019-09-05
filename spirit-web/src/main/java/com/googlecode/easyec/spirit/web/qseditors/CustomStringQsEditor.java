package com.googlecode.easyec.spirit.web.qseditors;

/**
 * 字符串类型的URL参数值编辑类
 *
 * @author JunJie
 */
public class CustomStringQsEditor extends AbstractQueryStringEditor {

    private static final long serialVersionUID = -1811249190240521226L;

    @Override
    protected String coerceOneObjectToQs(Object bean) {
        return bean instanceof String ? ((String) bean) : bean.toString();
    }

    @Override
    protected Object coerceOneValueToBean(String qs) {
        return qs;
    }
}
