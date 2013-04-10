package com.googlecode.easyec.zkoss.paging;

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
}
