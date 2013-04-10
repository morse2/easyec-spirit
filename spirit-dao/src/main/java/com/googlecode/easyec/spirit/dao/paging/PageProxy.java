package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;

/**
 * 分页对象代理类。
 *
 * @author JunJie
 */
public interface PageProxy<T extends Page> {

    /**
     * 通过分页方言创建一个分页代理对象。
     *
     * @param dialect 分页方言
     * @return 分页对象
     */
    T createProxy(PageDialect dialect);

    /**
     * 通过分页方言、当前页码、分页数，创建一个分页代理对象。
     *
     * @param dialect     分页方言
     * @param currentPage 当前页码
     * @param pageSize    分页数
     * @return 分页对象
     */
    T createProxy(PageDialect dialect, int currentPage, int pageSize);
}
