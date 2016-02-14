package com.googlecode.easyec.spirit.dao.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPageProxy;
import com.googlecode.easyec.spirit.dao.paging.JdbcPage;
import com.googlecode.easyec.spirit.dao.paging.JdbcPageWritable;
import com.googlecode.easyec.spirit.dao.paging.PageComputable;

import java.lang.reflect.Proxy;

/**
 * JDBC分页代理类，用于代理JDBC分页对象的实例。
 *
 * @author JunJie
 */
public class JdbcPageProxy extends AbstractPageProxy<JdbcPage> {

    private static final ThreadLocal<JdbcPageProxy> _t = new ThreadLocal<JdbcPageProxy>();

    public JdbcPage createProxy(PageDialect pageDialect) {
        return (JdbcPage) Proxy.newProxyInstance(
            JdbcPageProxy.class.getClassLoader(),
            new Class[] { JdbcPage.class, PageComputable.class, JdbcPageWritable.class },
            new PageInvocationHandler(new JdbcPageImpl(pageDialect))
        );
    }

    public JdbcPage createProxy(PageDialect pageDialect, int currentPage, int pageSize) {
        return (JdbcPage) Proxy.newProxyInstance(
            JdbcPageProxy.class.getClassLoader(),
            new Class[] { JdbcPage.class, PageComputable.class, JdbcPageWritable.class },
            new PageInvocationHandler(new JdbcPageImpl(pageDialect, currentPage, pageSize))
        );
    }

    public static JdbcPage newProxy(PageDialect dialect) {
        JdbcPageProxy p = _t.get();

        if (p == null) {
            p = new JdbcPageProxy();
            _t.set(p);
        }

        return p.createProxy(dialect);
    }

    public static JdbcPage newProxy(PageDialect dialect, int currentPage, int pageSize) {
        JdbcPageProxy p = _t.get();

        if (p == null) {
            p = new JdbcPageProxy();
            _t.set(p);
        }

        return p.createProxy(dialect, currentPage, pageSize);
    }
}
