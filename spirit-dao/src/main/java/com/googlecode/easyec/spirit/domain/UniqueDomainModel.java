package com.googlecode.easyec.spirit.domain;


/**
 * 表示唯一的域模型的接口类。
 * 子类需要实现该接口中的2方法即可
 *
 * @param <T> 表示唯一键的范型对象
 * @author junjie
 */
public interface UniqueDomainModel<T> {

    /**
     * 判断对象是否相等的方法，
     * 相等，则表示选中或取消选中。
     *
     * @param obj 对象
     * @return 比较是否相等
     */
    boolean evaluate(T obj);

    /**
     * 获取选中的值
     *
     * @return T 范型
     */
    T get();
}
