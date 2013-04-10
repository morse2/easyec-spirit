package com.googlecode.easyec.spirit.dao.dialect.impl;

import com.googlecode.easyec.spirit.dao.dialect.JdbcPageDialect;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * PostgreSQL数据库类型的分页方言实现。
 *
 * @author JunJie
 */
public class PostgreSqlJdbcPageDialect extends JdbcPageDialect {

    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(jdbcSql).append(" limit ? offset ?");

        logger.debug("Paged SQL for {}: [{}].", new Object[] { getDialectName(), sb });

        return sb.toString();
    }

    public int[] getPagedParameters(int currentPage, int pageSize) {
        int[] params = { pageSize, (currentPage - 1) * pageSize };
        logger.debug("Parameters of Paged SQL in MySQL: [" + Arrays.toString(params) + "].");

        return params;
    }

    public String getDialectName() {
        return "PostgreSQL";
    }
}
