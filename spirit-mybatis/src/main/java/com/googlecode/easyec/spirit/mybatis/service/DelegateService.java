package com.googlecode.easyec.spirit.mybatis.service;

import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;

import java.io.Serializable;
import java.util.List;

/**
 * 标准业务层模型代理DAO数据层的业务接口类
 *
 * @author JunJie
 */
public interface DelegateService<T extends DelegateDao<M, ID>, M extends GenericPersistentDomainModel<ID>, ID extends Serializable> {

    /**
     * 将模型持久化到数据库的方法。
     *
     * @param model 可持久化模型对象
     */
    void saveOrUpdate(M model) throws DataPersistenceException;

    /**
     * 通过模型主键，删除数据库中一条模型记录
     *
     * @param model 模型对象
     */
    void delete(M model) throws DataPersistenceException;

    /**
     * 通过模型主键查找模型数据对象
     *
     * @param uidPk 模型主键
     * @return 模型对象
     */
    M findByPK(ID uidPk);

    /**
     * 分页查找模型信息
     *
     * @param formBean 搜索条件
     * @return 分页结果对象
     */
    Page find(AbstractSearchFormBean formBean);

    /**
     * 分页查找模型信息
     *
     * @param formBean 搜索条件
     * @param pageSize 每页显示的记录数
     * @return 分页结果对象
     */
    Page find(AbstractSearchFormBean formBean, int pageSize);

    /**
     * 通过查询条件查找模型信息
     *
     * @param formBean 查询条件
     * @return 模型对象列表
     */
    List<M> search(AbstractSearchFormBean formBean);

    /**
     * 统计符合条件的数据总数
     *
     * @param formBean 查询条件
     * @return 数据总数
     */
    long count(AbstractSearchFormBean formBean);

    /**
     * 返回DAO数据层的委托类
     *
     * @return <code>DelegateDao</code>子类
     */
    T getDelegateDao();
}
