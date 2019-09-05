package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPageProxy;
import com.googlecode.easyec.spirit.dao.paging.PageComputable;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPage;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPageWritable;

import java.lang.reflect.Proxy;

/**
 * MyBatis分页代理类，用于代理MyBatis分页对象的实例。
 *
 * @author JunJie
 */
public class MybatisPageProxy extends AbstractPageProxy<MybatisPage> {

    private static final ThreadLocal<MybatisPageProxy> t = new ThreadLocal<MybatisPageProxy>();

    public MybatisPage createProxy(PageDialect dialect) {
        return (MybatisPage) Proxy.newProxyInstance(
                MybatisPageProxy.class.getClassLoader(),
                new Class[] {
                        MybatisPage.class,
                        MybatisPageWritable.class,
                        PageComputable.class
                },
            new PageInvocationHandler(new MybatisPageImpl(dialect))
        );
    }

    public MybatisPage createProxy(PageDialect dialect, int currentPage, int pageSize) {
        return (MybatisPage) Proxy.newProxyInstance(
                MybatisPageProxy.class.getClassLoader(),
                new Class[] {
                        MybatisPage.class,
                        MybatisPageWritable.class,
                        PageComputable.class
                },
            new PageInvocationHandler(new MybatisPageImpl(dialect, currentPage, pageSize))
        );
    }

    public static MybatisPage newProxy(PageDialect dialect) {
        MybatisPageProxy p = t.get();

        if (p == null) {
            p = new MybatisPageProxy();
            t.set(p);
        }

        return p.createProxy(dialect);
    }

    public static MybatisPage newProxy(PageDialect dialect, int currentPage, int pageSize) {
        MybatisPageProxy p = t.get();

        if (p == null) {
            p = new MybatisPageProxy();
            t.set(p);
        }

        return p.createProxy(dialect, currentPage, pageSize);
    }
}
