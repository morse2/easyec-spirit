package com.googlecode.easyec.spirit.domain;

import java.io.Serializable;

/**
 * 泛型类型的域模型对象。
 * <p>
 * 子类实现该类，可设置主键的泛型类型，
 * 并且该类表示了模型可以被持久化至数据库中。
 * </p>
 *
 * @author JunJie
 */
public interface GenericPersistentDomainModel<T extends Serializable> extends DomainModel {

    /**
     * 返回当前持久化对象的主键值
     *
     * @return {@link T}
     */
    T getUidPk();

    /**
     * 设置当前持久化对象的主键值
     *
     * @param uidPk {@link T}
     */
    void setUidPk(T uidPk);
}
