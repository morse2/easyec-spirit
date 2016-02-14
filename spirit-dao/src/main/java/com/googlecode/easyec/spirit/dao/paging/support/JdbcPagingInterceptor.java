package com.googlecode.easyec.spirit.dao.paging.support;

import com.googlecode.easyec.spirit.dao.paging.JdbcPage;
import com.googlecode.easyec.spirit.dao.paging.JdbcPageWritable;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.PagingInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 * JDBC分页拦截类
 *
 * @author JunJie
 */
@Aspect
public class JdbcPagingInterceptor extends NamedParameterJdbcDaoSupport implements PagingInterceptor, Ordered {

    private int order;

    /**
     * 设置AOP执行的顺序值。
     *
     * @param order 排序值。数值越大，优先权越低
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    @Around(value = "execution(* com.*..*.dao.paging.JdbcPageDao.find(..)) && args(page,..)")
    public Object intercept(ProceedingJoinPoint joinPoint, Page page) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length != 3) {
            return joinPoint.proceed(
                joinPoint.getArgs()
            );
        }

        return _doPaging(
            joinPoint, page,
            ((String) args[1]),
            (Class) args[2]
        );
    }

    private Object _doPaging(ProceedingJoinPoint jp, Page page, String sql, Class type) throws Throwable {
        if (!(page instanceof JdbcPage)) {
            logger.debug("The Page object isn't instanceof JdbcPage, so ignore.");

            return jp.proceed(jp.getArgs());
        }

        // must be created a new proxy every time.
        JdbcPage jdbcPage = JdbcPageProxy.newProxy(
            page.getPageDialect(),
            page.getCurrentPage(),
            page.getPageSize()
        );

        if (jdbcPage instanceof JdbcPageWritable) {
            ((JdbcPageWritable) jdbcPage).setSearchTerms(((JdbcPage) page).getSearchTerms());
        }

        new DefaultJdbcPagingWork(
            getNamedParameterJdbcTemplate(), sql, type
        ).doPaging(jdbcPage);

        return jdbcPage;
    }
}
