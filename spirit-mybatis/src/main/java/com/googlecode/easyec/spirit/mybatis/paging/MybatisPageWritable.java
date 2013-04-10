package com.googlecode.easyec.spirit.mybatis.paging;

import com.googlecode.easyec.spirit.dao.paging.PageWritable;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;

/**
 * MyBatis分页可写的方法接口定义类。
 *
 * @author JunJie
 */
public interface MybatisPageWritable extends PageWritable {

    /**
     * 设置当前分页带有的查询参数对象。
     * 对象可以是任何Java类型。
     *
     * @param parameterObject 查询条件的对象形式
     */
    void setParameterObject(Object parameterObject);

    /**
     * 设置当前分页带有的排序对象列表。
     *
     * @param sorts 排序对象列表。
     */
    void setSorts(List<Sort> sorts);
}
