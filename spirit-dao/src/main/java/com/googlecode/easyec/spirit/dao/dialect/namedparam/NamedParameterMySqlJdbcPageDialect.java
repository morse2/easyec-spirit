package com.googlecode.easyec.spirit.dao.dialect.namedparam;

import com.googlecode.easyec.spirit.dao.dialect.impl.MySqlJdbcPageDialect;
import org.apache.commons.lang.StringUtils;

/**
 * 参数名称化的JDBC支持MYSQL数据库的分页方言实现类
 *
 * @author JunJie
 */
public class NamedParameterMySqlJdbcPageDialect extends MySqlJdbcPageDialect {

    @Override
    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(jdbcSql).append(" limit :start, :end");

        logger.debug("Paged SQL for MySQL: [" + sb + "].");

        return sb.toString();
    }
}
