package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;
import java.util.Map;

/**
 * 支持JDBC分页查询的可写的方法的定义接口类
 *
 * @author JunJie
 */
public interface JdbcPageWritable extends PageWritable {

    /**
     * 设置表单查询参数集合
     *
     * @param params 查询参数
     */
    void setSearchTerms(Map<String, Object> params);

    /**
     * 设置排序参数集合
     *
     * @param sorts 排序集合
     */
    void setSorts(List<Sort> sorts);
}
