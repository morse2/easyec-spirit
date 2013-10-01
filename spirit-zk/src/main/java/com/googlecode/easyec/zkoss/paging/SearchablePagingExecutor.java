package com.googlecode.easyec.zkoss.paging;

import org.zkoss.zk.ui.Component;

/**
 * 可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public interface SearchablePagingExecutor extends PagingExecutor {

    /**
     * 触发分页执行的方法。
     *
     * @param currentPage     当前页码
     * @param withSearchTerms 标识是否带入搜索条件
     */
    void firePaging(int currentPage, boolean withSearchTerms);

    /**
     * 设置查询条件组件的搜索范围。
     * 从给定的组件下开始搜索。
     *
     * @param searchScope 搜索范围的组件对象
     */
    void setSearchScope(Component searchScope);
}
