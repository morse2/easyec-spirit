package com.googlecode.easyec.spirit.dao.dialect;

/**
 * 分页的方言接口定义类。
 *
 * @author JunJie
 */
public interface PageDialect extends SqlDialect {

    /**
     * 得到计算总记录数的SQL语句。
     *
     * @param jdbcSql 本地化SQL语句
     * @return SQL
     */
    String getCountSql(String jdbcSql);

    /**
     * 通过给定的本地SQL，转换成可以分页的SQL语句。
     *
     * @param jdbcSql 本地化SQL语句
     * @return 分页的SQL语句
     */
    String getPagedSql(String jdbcSql);

    /**
     * 返回得到分页的参数。
     * <p>
     * 分页参数包括：offset和limit
     * </p>
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示的记录数
     * @return int[]
     */
    public int[] getPagedParameters(int currentPage, int pageSize);
}
