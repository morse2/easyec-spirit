package com.googlecode.easyec.spirit.mybatis.mapper;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据层操作的委托模型接口类。
 * <p>
 * 该类定义了操作数据库表的基本方法，
 * 例如：增、删、改、查、分页等方法。
 * </p>
 *
 * @author JunJie
 */
public interface DelegateDao<M extends GenericPersistentDomainModel<ID>, ID extends Serializable> {

    /**
     * 将模型持久化到数据库的方法。
     *
     * @param model 可持久化模型对象
     * @return 影响的行数
     */
    int insert(M model);

    /**
     * 通过模型主键，删除数据库中一条模型记录
     *
     * @param uidPk 模型的主键
     * @return 影响的行数
     */
    int deleteByPrimaryKey(ID uidPk);

    /**
     * 通过模型主键查找模型数据对象
     *
     * @param uidPk 模型主键
     * @return 模型对象
     */
    M selectByPrimaryKey(ID uidPk);

    /**
     * 更新给定的模型数据
     *
     * @param model 模型对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(M model);

    /**
     * 分页查找模型信息
     *
     * @param page 分页对象，搜索条件
     * @return 分页结果对象
     */
    Page find(Page page);

    /**
     * 通过查询条件查找模型信息
     *
     * @param params 查询条件
     * @return 模型对象列表
     */
    List<M> find(Map<String, Object> params);

    /**
     * 通过查询条件统计符合条件的记录总数
     *
     * @param params 查询条件
     * @return 记录总数
     */
    long count(Map<String, Object> params);
}
