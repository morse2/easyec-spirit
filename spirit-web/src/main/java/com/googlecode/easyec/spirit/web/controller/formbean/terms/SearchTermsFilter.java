package com.googlecode.easyec.spirit.web.controller.formbean.terms;

/**
 * 搜索条件过滤器类
 * <p>
 * 此类用于过滤不符合条件的搜索条件
 * </p>
 *
 * @author JunJie
 */
public interface SearchTermsFilter {

    /**
     * 判断方法。
     * 返回真表示接受该搜索条件，
     * 返回假表示不接受搜索条件。
     *
     * @param k 搜索条件的KEY
     * @param v 搜索条件的VAL
     * @return bool值
     */
    boolean accept(String k, Object v);
}
