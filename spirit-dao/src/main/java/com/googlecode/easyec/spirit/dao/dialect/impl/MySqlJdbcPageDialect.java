package com.googlecode.easyec.spirit.dao.dialect.impl;

import com.googlecode.easyec.spirit.dao.dialect.JdbcPageDialect;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * MySQL数据库类型的分页方言实现。
 *
 * @author JunJie
 */
public class MySqlJdbcPageDialect extends JdbcPageDialect {

    public String getDialectName() {
        return "MySQL";
    }

    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(jdbcSql).append(" limit ?, ?");

        logger.debug("Paged SQL for MySQL: [" + sb + "].");

        return sb.toString();
    }

    public int[] getPagedParameters(int currentPage, int pageSize) {
        int[] params = { (currentPage - 1) * pageSize, pageSize };
        logger.debug("Parameters of Paged SQL in MySQL: [" + Arrays.toString(params) + "].");

        return params;
    }
}
