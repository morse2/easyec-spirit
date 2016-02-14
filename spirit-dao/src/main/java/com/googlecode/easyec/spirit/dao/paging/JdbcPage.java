package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;
import java.util.Map;

/**
 * 支持JDBC分页查询的接口类
 *
 * @author JunJie
 */
public interface JdbcPage extends Page {

    /**
     * 返回当前分页的表单搜索参数集合
     *
     * @return 查询参数
     */
    Map<String, Object> getSearchTerms();

    /**
     * 返回当前分页的排序参数集合
     */
    List<Sort> getSorts();
}
