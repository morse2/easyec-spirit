package com.googlecode.easyec.spirit.web.webservice.ui.paging.impl;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.dialect.impl.NoOpPageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPageProxy;
import com.googlecode.easyec.spirit.dao.paging.PageComputable;
import com.googlecode.easyec.spirit.web.webservice.ui.paging.WebPage;

import java.lang.reflect.Proxy;

/**
 * 支持WS分页的代理类
 *
 * @author JunJie
 */
public class WebPageProxy extends AbstractPageProxy<WebPage> {

    private static final ThreadLocal<WebPageProxy> _t = new ThreadLocal<>();

    @Override
    public WebPage createProxy(PageDialect dialect) {
        return (WebPage) Proxy.newProxyInstance(
            WebPageProxy.class.getClassLoader(),
            new Class[] { WebPage.class, PageComputable.class },
            new PageInvocationHandler(new DefaultWebPage(dialect))
        );
    }

    @Override
    public WebPage createProxy(PageDialect dialect, int currentPage, int pageSize) {
        return (WebPage) Proxy.newProxyInstance(
            WebPageProxy.class.getClassLoader(),
            new Class[] { WebPage.class, PageComputable.class },
            new PageInvocationHandler(new DefaultWebPage(dialect, currentPage, pageSize))
        );
    }

    public static WebPage newProxy() {
        WebPageProxy p = _t.get();

        if (p == null) {
            p = new WebPageProxy();
            _t.set(p);
        }

        return p.createProxy(new NoOpPageDialect());
    }

    public static WebPage newProxy(int currentPage, int pageSize) {
        WebPageProxy p = _t.get();

        if (p == null) {
            p = new WebPageProxy();
            _t.set(p);
        }

        return p.createProxy(new NoOpPageDialect(), currentPage, pageSize);
    }
}
