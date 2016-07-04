package com.googlecode.easyec.spirit.dao.dialect;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * JDBC的SQL方言实现类。
 * 此方法实现了{@link SqlDialect#getSortedSql(String, java.util.List)}方法。
 *
 * @author JunJie
 */
public abstract class JdbcSqlDialect implements SqlDialect {

    protected static final Pattern order_By_Pattern = compile("\\s*order\\s*by[\\s\\S\\w\\W]*", CASE_INSENSITIVE);

    /**
     * SLF4J logger object
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public String getSortedSql(String jdbcSql, List<String> sorts) {
        if (StringUtils.isBlank(jdbcSql)) return "";
        if (CollectionUtils.isEmpty(sorts)) return jdbcSql;

        logger.debug("Original JDBC sql: [" + jdbcSql + "].");
        String sql = jdbcSql;

        StringBuffer sb = new StringBuffer();
        Matcher m = order_By_Pattern.matcher(sql);
        if (m.find()) {
            sb.append(m.replaceAll("")).append(" order by ");
        } else {
            sb.append(sql).append(" order by ");
        }

        int len = sb.length();
        for (String so : sorts) {
            if (sb.length() > len) sb.append(", ");

            sb.append(so);
        }

        return sb.toString();
    }
}
