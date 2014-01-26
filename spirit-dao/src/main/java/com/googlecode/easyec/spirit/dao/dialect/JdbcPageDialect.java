package com.googlecode.easyec.spirit.dao.dialect;

import org.apache.commons.lang.StringUtils;

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
    protected static final Pattern select_Pattern_1 = compile("[\\w\\W\\s]*from\\s*", CASE_INSENSITIVE);
    /* distinct pattern */
    protected static final Pattern select_Pattern_2 = compile("(distinct)\\s+[\\w\\W]*", CASE_INSENSITIVE);
    /* from pattern */
    protected static final Pattern select_Pattern_f = compile("\\s*from\\s*", CASE_INSENSITIVE);
    /* group by pattern */
    protected static final Pattern select_Pattern_3 = compile("\\s*group\\s*by[\\s\\S\\w\\W]*", CASE_INSENSITIVE);
    /* union pattern */
    protected static final Pattern select_Pattern_u = compile("\\s*union\\s*", CASE_INSENSITIVE);

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

            return sb.append(") _t_0").toString();
        }

        return doGetCountSql(jdbcSql);
    }

    private String doGetCountSql(String sql) {
        StringBuffer sb = new StringBuffer();
        // remove order by statement if exists
        Matcher m1 = order_By_Pattern.matcher(sql);
        if (m1.find()) {
            m1.appendReplacement(sb, "");
        }

        sql = m1.appendTail(sb).toString();

        logger.debug("JDBC SQL after removing 'order by' statement. SQL: [" + sql + "].");

        StringBuffer sb1 = new StringBuffer();

        String countSql;
        // resolve group by statement
        Matcher m4 = select_Pattern_3.matcher(sql);
        if (m4.find()) {
            countSql = "select count(*) from (" + sql + ") _t_1";
        } else {
            // replace count sql
            Matcher m2 = select_Pattern_1.matcher(sql);
            if (m2.find()) {
                String replaceStr = "select count(*) from ";
                String group = m2.group();

                if (m4.find()) {
                    replaceStr = "select count(*) from (" + group + ") _t_1";
                } else {
                    Matcher m3 = select_Pattern_2.matcher(group);
                    if (m3.find()) {
                        String g = m3.group();
                        Matcher m3_from = select_Pattern_f.matcher(g);
                        if (m3_from.find()) {
                            g = m3_from.replaceAll("");
                        }

                        replaceStr = "select count(" + g + ") from ";
                    }
                }

                m2.appendReplacement(sb1, replaceStr);
            }

            countSql = m2.appendTail(sb1).toString();
        }

        logger.info("Native JDBC SQL for counting: [" + countSql + "].");

        return countSql;
    }
}
