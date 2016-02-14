package com.googlecode.easyec.spirit.dao.paging.config;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.JdbcPage;
import com.googlecode.easyec.spirit.dao.paging.JdbcPageWritable;
import com.googlecode.easyec.spirit.dao.paging.PageProxy;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;

/**
 * 默认内部的Mybatis分页委托类。
 *
 * @author JunJie
 */
final class DefaultJdbcPageDelegate extends PageDelegate<JdbcPage> {

    public DefaultJdbcPageDelegate(Integer pageSize, PageDialect pageDialect, PageProxy<JdbcPage> pageProxy) {
        super(pageSize, pageDialect, pageProxy);
    }

    @Override
    public JdbcPage createPage(AbstractSearchFormBean abstractSearchFormBean) {
        return createPage(abstractSearchFormBean, getPageSize());
    }

    @Override
    public JdbcPage createPage(AbstractSearchFormBean bean, int i) {
        JdbcPage page = createPage(bean.getPageNumber(), i);
        if (page instanceof JdbcPageWritable) {
            ((JdbcPageWritable) page).setSearchTerms(bean.getSearchTerms());
        }

        return page;
    }
}
