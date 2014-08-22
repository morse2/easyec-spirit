package com.googlecode.easyec.spirit.mybatis.query;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.mybatis.service.DelegateService;
import com.googlecode.easyec.spirit.query.AbstractQuery;

import java.util.List;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.autowireBeanProperties;

/**
 * 抽象的委托业务代理的查询类
 *
 * @author JunJie
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDelegateQuery<T> extends AbstractQuery<T> {

    protected AbstractDelegateQuery() {
        autowireBeanProperties(this, true);
    }

    public Page listPage(int currentPage) {
        setPageNumber(currentPage);
        return getDelegateService().find(getSearchFormBean());
    }

    public Page listPage(int currentPage, int pageSize) {
        setPageNumber(currentPage);
        return getDelegateService().find(getSearchFormBean(), pageSize);
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
