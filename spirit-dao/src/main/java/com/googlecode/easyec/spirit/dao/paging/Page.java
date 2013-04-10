package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;

import java.util.List;

/**
 * 分页接口类.
 *
 * @author JunJie
 */
public interface Page {

    /**
     * 判断下一页是否可用.
     *
     * @return true or false
     */
    boolean getNextPageAvailable();

    /**
     * 判断上一页是否可用.
     *
     * @return true or false
     */
    boolean getPrevPageAvailable();

    /**
     * 得到当前页面的页数号.
     *
     * @return int
     */
    int getCurrentPage();

    /**
     * 得到每页显示的记录数.
     *
     * @return int
     */
    int getPageSize();

    /**
     * 得到分页的总页数.
     *
     * @return int
     */
    int getTotalSize();

    /**
     * 得到当前分页的数据.
     *
     * @return <code>java.util.List</code>
     */
    List<?> getRecords();

    /**
     * 得到总的记录数量.
     *
     * @return int
     */
    int getTotalRecordsCount();

    /**
     * 得到当前页的记录编号的迭代器对象.
     *
     * @return <code>java.util.Iterator</code>
     */
    PageLineIterator<Integer> getPageLineIterator();

    /**
     * 得到当前系统数据库的分页方言。
     *
     * @return <ocde>PageDialect</ocde>
     * @see PageDialect
     */
    PageDialect getPageDialect();
}
