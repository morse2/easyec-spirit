package com.googlecode.easyec.spirit.dao.paging;

import org.aspectj.lang.ProceedingJoinPoint;

import java.sql.SQLException;

/**
 * 分页拦截器类。此类用于拦截Dao层的分页方法，
 * 在不改变原有SQL语句的结构的基础上，实现分页算法。
 *
 * @author JunJie
 */
public interface PagingInterceptor {

    /**
     * 拦截Dao层方法的方法定义。在此方法中，
     * 判断并实现分页的算法逻辑运算。
     *
     * @param joinPoint AOP切入点对象
     * @param page      分页对象
     * @return 分页的结果
     * @throws Throwable
     */
    public Object intercept(ProceedingJoinPoint joinPoint, Page page) throws Throwable;

    /**
     * 内部类。分页具体的工作类。
     *
     * @param <T> {@link Page}对象实例
     */
    public interface PagingWork<T extends Page> {

        /**
         * 执行分页的算法实现的方法。
         * 根据给定的分页对象，进行分页的计算。
         *
         * @param page {@link Page}对象实例
         * @throws PagingException 分页发生异常，则需要抛出此类型的异常
         * @throws SQLException
         */
        void doPaging(T page) throws PagingException, SQLException;
    }
}
