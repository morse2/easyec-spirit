package com.googlecode.easyec.spirit.mybatis.service;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;

import java.io.Serializable;

/**
 * 委托模型业务接口扩展控制类。
 * <p>
 * 业务实现类扩展该接口后，
 * 子类被实例化时候该接口中的方法会被执行
 * </p>
 *
 * @author JunJie
 */
public interface DelegateServiceCtrl<T extends DelegateDao<M, ID>,
    M extends GenericPersistentDomainModel<ID>,
    ID extends Serializable> {

    /**
     * 为业务子类传递委托的数据层代理对象实例
     *
     * @param delegateDao {@link DelegateDao}
     */
    void setDelegateDao(T delegateDao);
}
