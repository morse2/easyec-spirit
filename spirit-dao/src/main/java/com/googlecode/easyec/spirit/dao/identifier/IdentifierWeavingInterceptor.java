package com.googlecode.easyec.spirit.dao.identifier;

import org.aspectj.lang.JoinPoint;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 主键标识织入拦截器类。
 *
 * @author JunJie
 */
public interface IdentifierWeavingInterceptor<T> {

    /**
     * 拦截方法。
     *
     * @param joinPoint AOP切入点对象
     * @param o         PO对象
     * @throws Throwable
     */
    void weaveBefore(JoinPoint joinPoint, T o) throws Throwable;

    /**
     * 隔离的工作类。此类用于在隔离范围中，执行计算主键序列值。
     *
     * @param <T>
     */
    public interface IsolatedWork<T extends Serializable> {

        /**
         * 执行隔离工作的方法。
         *
         * @param conn 数据库连接
         * @return 新的序列值
         * @throws SQLException
         */
        T doIsolatedWork(Connection conn) throws SQLException;
    }
}
