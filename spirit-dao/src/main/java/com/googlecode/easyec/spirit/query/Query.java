package com.googlecode.easyec.spirit.query;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;

import java.util.List;

/**
 * 封装数据层查询方法的类
 *
 * @author JunJie
 */
public interface Query<T> {

    /**
     * 返回第几页的分页数据
     *
     * @param currentPage 当前页码
     * @return 查询对象实例
     */
    Page listPage(int currentPage);

    /**
     * 返回第几页的分页数据，
     * 并指定显示的记录数。
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示的记录数
     * @return 查询对象实例
     */
    Page listPage(int currentPage, int pageSize);

    /**
     * 计算数据的总数
     *
     * @return 数据总数
     */
    long count();

    /**
     * 查询数据的方法
     *
     * @param <U> 实体对象的泛型类型
     * @return 符合条件的数据集合
     */
    <U> List<U> list();

    /**
     * 返回封装过的表单搜索对象
     *
     * @return <code>AbstractSearchFormBean</code>对象实例
     * @see AbstractSearchFormBean
     */
    AbstractSearchFormBean getSearchFormBean();

    /**
     * 合并给定的表单搜索对象实例。
     * 此方法会拷贝参数中的各个搜索条件
     * 以及原始配置信息
     *
     * @param bean <code>AbstractSearchFormBean</code>对象实例
     * @return 查询对象实例
     */
    T searchFormBean(AbstractSearchFormBean bean);
}
