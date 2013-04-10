package com.googlecode.easyec.spirit.dao.dialect.impl;

import com.googlecode.easyec.spirit.dao.dialect.JdbcPageDialect;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Oracle数据库类型的分页方言实现类。
 *
 * @author JunJie
 */
public class OracleJdbcPageDialect extends JdbcPageDialect {

    public String getDialectName() {
        return "Oracle";
    }

    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        // build paged sql
        StringBuffer sb = new StringBuffer();
        sb.append("select * from (select t_.*, ROWNUM rn from (");
        sb.append(jdbcSql);
        sb.append(") t_ where ROWNUM <= ?) where rn > ?");

        logger.debug("Paged SQL for Oracle: [" + sb + "].");

        return sb.toString();
    }

    public int[] getPagedParameters(int currentPage, int pageSize) {
        int[] params = { (currentPage - 1) * pageSize, currentPage * pageSize };
        logger.debug("Parameters of Paged SQL in Oracle: [" + Arrays.toString(params) + "].");

        return params;
    }
}
