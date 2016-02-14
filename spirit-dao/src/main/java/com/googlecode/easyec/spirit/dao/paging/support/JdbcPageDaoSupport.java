package com.googlecode.easyec.spirit.dao.paging.support;

import com.googlecode.easyec.spirit.dao.paging.JdbcPage;
import com.googlecode.easyec.spirit.dao.paging.JdbcPageDao;
import com.googlecode.easyec.spirit.dao.paging.Page;

/**
 * 支持JDBC分页查询的DAO的志愿类.
 * 该类中的方法为空方法实现,具体的分页
 * 实现需配合{@link JdbcPagingInterceptor}
 * 拦截类共同使用.
 *
 * @author JunJie
 * @see JdbcPagingInterceptor
 */
public final class JdbcPageDaoSupport implements JdbcPageDao {

    @Override
    public final Page find(JdbcPage page, String sql, Class type) {
        // no op
        return null;
    }
}
