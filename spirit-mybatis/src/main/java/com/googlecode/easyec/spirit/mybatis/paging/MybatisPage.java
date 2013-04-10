package com.googlecode.easyec.spirit.mybatis.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;

/**
 * MyBatis的分页接口类。
 *
 * @author JunJie
 */
public interface MybatisPage extends Page {

    /**
     * 返回当前分页带有的查询参数对象。
     *
     * @return 查询条件对象
     */
    Object getParameterObject();

    /**
     * 返回当前分页带有的排序对象列表。
     *
     * @return 排序对象列表
     */
    List<Sort> getSorts();
}
