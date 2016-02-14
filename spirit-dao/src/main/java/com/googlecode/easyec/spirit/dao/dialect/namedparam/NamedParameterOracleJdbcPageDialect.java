package com.googlecode.easyec.spirit.dao.dialect.namedparam;

import com.googlecode.easyec.spirit.dao.dialect.impl.OracleJdbcPageDialect;
import org.apache.commons.lang.StringUtils;

/**
 * 参数名称化的JDBC支持ORACLE的分页的方言实现类
 *
 * @author JunJie
 */
public class NamedParameterOracleJdbcPageDialect extends OracleJdbcPageDialect {

    @Override
    public String getPagedSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append("select * from (select t_.*, ROWNUM rn from (");
        sb.append(jdbcSql);
        sb.append(") t_ where ROWNUM <= :start) where rn > :end");

        this.logger.debug("Paged SQL for Oracle: [" + sb + "].");

        return sb.toString();
    }
}
