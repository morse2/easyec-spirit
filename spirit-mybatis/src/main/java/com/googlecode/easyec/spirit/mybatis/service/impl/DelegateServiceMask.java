package com.googlecode.easyec.spirit.mybatis.service.impl;

import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import com.googlecode.easyec.spirit.mybatis.service.DelegateServiceCtrl;
import com.googlecode.easyec.spirit.service.EcService;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;

import java.io.Serializable;
import java.util.List;

/**
 * 标准业务层模型代理DAO数据层的业务接口类
 *
 * @author JunJie
 */
public class DelegateServiceMask<T extends DelegateDao<M, ID>, M extends GenericPersistentDomainModel<ID>, ID extends Serializable>
    extends EcService implements DelegateService<T, M, ID>, DelegateServiceCtrl<T, M, ID> {

    private T delegateDao;

    public void saveOrUpdate(M model) throws DataPersistenceException {
        logger.trace("Implementation via proxy class.");
    }

    public void delete(M model) throws DataPersistenceException {
        logger.trace("Implementation via proxy class.");
    }

    public M findByPK(ID uidPk) {
        logger.trace("Implementation via proxy class.");

        return null;
    }

    public Page find(AbstractSearchFormBean formBean) {
        logger.trace("Implementation via proxy class.");

        return null;
    }

    public Page find(AbstractSearchFormBean formBean, int pageSize) {
        logger.trace("Implementation via proxy class.");

        return null;
    }

    public List<M> search(AbstractSearchFormBean bean) {
        logger.trace("Implementation via proxy class.");

        return null;
    }

    public long count(AbstractSearchFormBean formBean) {
        logger.trace("Implementation via proxy class.");

        return 0;
    }

    public T getDelegateDao() {
        return delegateDao;
    }

    public void setDelegateDao(T delegateDao) {
        this.delegateDao = delegateDao;
    }
}
