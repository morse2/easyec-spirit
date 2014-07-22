package com.googlecode.easyec.spirit.dao.dialect.impl;

import com.googlecode.easyec.spirit.dao.dialect.JdbcPageDialect;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;

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

        // 修复如果sql中同时包含distinct和order by
        // 关键字查询产生报错的问题
        Matcher m_d = select_Pattern_d.matcher(jdbcSql);
        Matcher m_o = order_By_Pattern.matcher(jdbcSql);
        // 如果同时查找两个关键字，则剔除distinct
        if (m_d.find() && m_o.find()) {
            StringBuffer buf = new StringBuffer();

            buf.append("select distinct * from (");
            m_d.appendReplacement(buf, "");
            buf.append(
                m_d.group().substring(m_d.start() + 1)
            );
            buf.append(") ");

            sb.append(buf);
        } else sb.append(jdbcSql);

        sb.append(") t_ where ROWNUM <= ?) where rn > ?");

        logger.debug("Paged SQL for Oracle: [" + sb + "].");

        return sb.toString();
    }

    public int[] getPagedParameters(int currentPage, int pageSize) {
        int[] params = { currentPage * pageSize, (currentPage - 1) * pageSize };
        logger.debug("Parameters of Paged SQL in Oracle: [" + Arrays.toString(params) + "].");

        return params;
    }
}
