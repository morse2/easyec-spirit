package com.googlecode.easyec.spirit.web.webservice.ui.paging.impl;

import com.googlecode.easyec.spirit.dao.dialect.impl.NoOpPageDialect;
import com.googlecode.easyec.spirit.dao.paging.PageProxy;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.webservice.ui.paging.WebPage;

/**
 * 支持WS分页的分页委托类
 *
 * @author JunJie
 */
public class WebPageDelegate extends PageDelegate<WebPage> {

    public WebPageDelegate(Integer pageSize, PageProxy<WebPage> pageProxy) {
        super(pageSize, new NoOpPageDialect(), pageProxy);
    }

    @Override
    public WebPage createPage(AbstractSearchFormBean bean) {
        return createPage(bean, getPageSize());
    }

    @Override
    public WebPage createPage(AbstractSearchFormBean bean, int pageSize) {
        WebPage page = createPage(bean.getPageNumber(), pageSize);
        page.setParameters(bean.getSearchTermsAsText());

        return page;
    }
}
