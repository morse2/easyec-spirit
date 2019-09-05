package com.googlecode.easyec.spirit.web.qseditors;

import java.io.Serializable;

/**
 * 查询参数编辑器类
 *
 * @author JunJie
 */
public interface QueryStringEditor extends Serializable {

    /**
     * 判断是否对给定的Bean对象
     * 进行转换字符串的操作。
     *
     * @param bean bean对象
     * @return 返回真，表示需要进行转换操作
     * @see #coerceToQs(Object)
     */
    boolean accept(Object bean);

    /**
     * 将给定的Bean对象转换成URL
     * 字符串参数值
     *
     * @param bean bean对象
     * @return 字符串值
     */
    String[] coerceToQs(Object bean);

    /**
     * 将给定的URL字符串参数值
     * 转换成应用所需的Bean对象类型
     *
     * @param qs URL字符串参数值
     * @return bean对象
     */
    Object coerceToBean(String[] qs);
}
