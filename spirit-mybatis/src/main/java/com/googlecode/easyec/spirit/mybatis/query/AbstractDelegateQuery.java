package com.googlecode.easyec.spirit.mybatis.query;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import com.googlecode.easyec.spirit.query.AbstractQuery;

import java.util.List;

/**
 * 抽象的委托业务代理的查询类
 *
 * @author JunJie
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDelegateQuery<T> extends AbstractQuery<T> {

    public Page listPage(int currentPage) {
        setPageNumber(currentPage);
        return getDelegateService().find(getSearchFormBean());
    }

    public Page listPage(int currentPage, int pageSize) {
        setPageNumber(currentPage);
        return getDelegateService().find(getSearchFormBean(), pageSize);
    }

    public long count() {
        return getDelegateService().count(getSearchFormBean());
    }

    public <U> List<U> list() {
        return getDelegateService().search(getSearchFormBean());
    }

    /**
     * 返回当前的委托代理业务接口的实现类
     *
     * @return 委托代理业务对象
     */
    abstract protected DelegateService getDelegateService();
}
