package com.googlecode.easyec.spirit.dao.dialect;

import java.util.List;

/**
 * SQL方言接口定义类。
 *
 * @author JunJie
 */
public interface SqlDialect {

    /**
     * 返回当前数据库的方言类型。
     *
     * @return 例如：MySQL, Oracle
     */
    String getDialectName();

    /**
     * 通过给定的本地SQL，组装ORDER BY排序条件。
     *
     * @param jdbcSql 本地化SQL语句
     * @param sorts   排序字段列表
     * @return 排序的SQL语句
     */
    String getSortedSql(String jdbcSql, List<String> sorts);
}
