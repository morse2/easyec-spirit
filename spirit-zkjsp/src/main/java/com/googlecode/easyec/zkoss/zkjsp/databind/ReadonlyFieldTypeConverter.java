package com.googlecode.easyec.zkoss.zkjsp.databind;

import org.zkoss.zk.ui.Component;

import java.util.Map;

/**
 * 只读属性数据类型转换基类
 *
 * @author JunJie
 */
public abstract class ReadonlyFieldTypeConverter<B, U> extends AbstractTypeConverter {

    @SuppressWarnings("unchecked")
    public Object coerceToUi(Object val, Component comp) {
        return coerceToUi((B) val, getBindingArgs(comp));
    }

    public final Object coerceToBean(Object val, Component comp) {
        return null;
    }

    /**
     * 将解析后的参数值转成需要的值类型
     *
     * @param val         参数的原始值
     * @param bindingArgs 参数的绑定映射
     * @return 转换后的值
     */
    abstract public U coerceToUi(B val, Map<String, Object> bindingArgs);
}
