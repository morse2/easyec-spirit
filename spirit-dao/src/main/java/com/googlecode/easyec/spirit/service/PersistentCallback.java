package com.googlecode.easyec.spirit.service;

/**
 * 持久化数据的回调类。
 *
 * @author JunJie
 */
public interface PersistentCallback {

    /**
     * 执行持久化数据的方法。
     *
     * @return 影响的记录数
     * @throws Exception 持久化数据异常类
     */
    int perform() throws Exception;
}
