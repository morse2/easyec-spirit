package com.googlecode.easyec.spirit.domain;

/**
 * 可持久化的域模型类。
 * <p>
 * 实现此类，可以表示此域模型可被持久化到数据库。
 * </p>
 *
 * @author JunJie
 */
public interface PersistentDomainModel extends DomainModel {

    /**
     * 返回当前持久化对象的主键值
     *
     * @return {@link Long}
     */
    Long getUidPk();

    /**
     * 设置当前持久化对象的主键值
     *
     * @param uidPk {@link Long}
     */
    void setUidPk(Long uidPk);
}
