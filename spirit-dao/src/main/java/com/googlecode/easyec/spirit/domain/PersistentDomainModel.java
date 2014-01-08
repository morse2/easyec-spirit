package com.googlecode.easyec.spirit.domain;

/**
 * 可持久化的域模型类。
 * <p>
 * 此类指定泛型的主键类型为长整型对象
 * </p>
 *
 * @author JunJie
 */
public interface PersistentDomainModel extends GenericPersistentDomainModel<Long> {
}
