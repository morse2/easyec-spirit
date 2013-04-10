package com.googlecode.easyec.spirit.dao.paging.factory;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.PageProxy;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;

/**
 * 分页对象委托模型类。
 *
 * @author JunJie
 */
public abstract class PageDelegate<T extends Page> {

    private Integer pageSize;
    private PageDialect pageDialect;
    private PageProxy<T> pageProxy;

    /**
     * 构造方法。
     *
     * @param pageSize    分页页数
     * @param pageDialect 分页方言对象
     * @param pageProxy   分页代理对象
     */
    public PageDelegate(Integer pageSize, PageDialect pageDialect, PageProxy<T> pageProxy) {
        this.pageSize = pageSize;
        this.pageDialect = pageDialect;
        this.pageProxy = pageProxy;
    }

    /**
     * 返回每页分页数。
     *
     * @return 大于0的正整数
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 返回分页方言类。
     *
     * @return {@link PageDelegate}实例对象
     */
    protected PageDialect getPageDialect() {
        return pageDialect;
    }

    /**
     * 返回分页代理对象
     *
     * @return {@link PageProxy} 实例对象
     */
    protected PageProxy<T> getPageProxy() {
        return pageProxy;
    }

    /**
     * 委托创建分页对象实例。
     *
     * @param currentPage 当前页码
     * @param pageSize    分页页数
     * @return 分页对象实例
     */
    public T createPage(int currentPage, int pageSize) {
        return pageProxy.createProxy(pageDialect, currentPage, pageSize);
    }

    abstract public T createPage(AbstractSearchFormBean bean);
}
