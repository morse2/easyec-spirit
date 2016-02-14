package com.googlecode.easyec.spirit.dao.paging;

/**
 * 支持JDBC分页操作的DAO接口类
 *
 * @author JunJie
 */
public interface JdbcPageDao {

    /**
     * JDBC的分页方法
     *
     * @param page <code>JdbcPage</code>对象实例
     * @param sql  <code>查询语句</code>
     * @param type <code>返回的查询结果集的对象类型</code>
     * @return <code>Page</code>对象
     */
    Page find(JdbcPage page, String sql, Class type);
}
