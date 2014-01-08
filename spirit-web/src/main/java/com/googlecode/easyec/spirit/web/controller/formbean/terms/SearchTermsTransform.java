package com.googlecode.easyec.spirit.web.controller.formbean.terms;

/**
 * 搜索条件的转换接口类
 *
 * @author JunJie
 */
public interface SearchTermsTransform {

    /**
     * 将给定的参数值对象转换为所需的对象。
     *
     * @param id  参数的键
     * @param val 参数的值
     * @return 转换后的参数对象
     */
    Object transform(String id, Object val);
}
