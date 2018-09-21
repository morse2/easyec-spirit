package com.googlecode.easyec.spirit.dao.dialect;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * 分页方言的Jdbc实现类。
 *
 * @author JunJie
 */
public abstract class JdbcPageDialect extends JdbcSqlDialect implements PageDialect {

    /* common pattern */
    protected static final Pattern select_Pattern_m = compile("\\s+from\\s+", CASE_INSENSITIVE);
    /* distinct pattern */
    protected static final Pattern select_Pattern_d = compile("(distinct)\\s+[\\w\\W]*", CASE_INSENSITIVE);
    /* group by pattern */
    protected static final Pattern select_Pattern_g = compile("\\s*group\\s*by[\\s\\S\\w\\W]*", CASE_INSENSITIVE);
    /* union pattern */
    protected static final Pattern select_Pattern_u = compile("\\s+union\\s+", CASE_INSENSITIVE);

    private static final String MARKUP = "__|__";

    public String getCountSql(String jdbcSql) {
        if (StringUtils.isBlank(jdbcSql)) {
            return "";
        }

        logger.debug("Original JDBC sql: [" + jdbcSql + "].");

        // matches whether has union keyword
        Matcher um = select_Pattern_u.matcher(jdbcSql);
        if (um.find()) {
            StringBuffer sb = new StringBuffer();
            sb.append("select count(*) from (");

            String[] parts = jdbcSql.split(um.group());
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) sb.append(" union ");
                String sql = parts[i];
                logger.debug("Part of sql: [{}].", sql);

                Matcher m1 = order_By_Pattern.matcher(sql);
                if (m1.find()) sb.append(m1.replaceAll(""));
                else sb.append(sql);
            }

            return sb.append(") t_0").toString();
        }

        return doGetCountSql(jdbcSql);
    }

    private String doGetCountSql(String sql) {
        StringBuffer sb = new StringBuffer();
        // remove order by statement if exists
        Matcher mo = order_By_Pattern.matcher(sql);
        if (mo.find()) mo.appendReplacement(sb, "");

        sql = mo.appendTail(sb).toString();

        logger.debug("JDBC SQL after removing 'order by' statement. SQL: [" + sql + "].");

        // matches distinct sql
        Matcher md = select_Pattern_d.matcher(sql);
        if (md.find()) {
            return new StringBuffer()
                .append("select count(*) from (")
                .append(sql)
                .append(") t_1")
                .toString();
        }

        String countSql;
        // resolve group by statement
        Matcher m4 = select_Pattern_g.matcher(sql);
        if (m4.find()) {
            return new StringBuffer()
                .append("select count(*) from (")
                .append(sql)
                .append(") t_2")
                .toString();
        }

        sb = new StringBuffer();
        // replace count sql
        Matcher mm = select_Pattern_m.matcher(sql);
        if (mm.find()) {
            mm.appendReplacement(sb, MARKUP + " select count(*) from ");
        }

        countSql = mm.appendTail(sb).toString();

        int i = countSql.indexOf(MARKUP) + MARKUP.length();
        if (i > 0) countSql = countSql.substring(i);

        logger.info("Native JDBC SQL for counting: [" + countSql + "].");

        return countSql;
    }
}
