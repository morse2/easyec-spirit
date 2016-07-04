package com.googlecode.easyec.spirit.dao.dialect.namedparam;

import com.googlecode.easyec.spirit.dao.dialect.impl.PostgreSqlJdbcPageDialect;
import org.apache.commons.lang3.StringUtils;

/**
 * 参数名称化的JDBC支持PostgreSQL数据库的分页方言实现类
 *
 * @author JunJie
 */
public class NamedParameterPostgreSqlJdbcPageDialect extends PostgreSqlJdbcPageDialect {

    @Override
    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(jdbcSql).append(" limit :start offset :end");

        logger.debug("Paged SQL for {}: [{}].", new Object[] { getDialectName(), sb });

        return sb.toString();
    }
}
