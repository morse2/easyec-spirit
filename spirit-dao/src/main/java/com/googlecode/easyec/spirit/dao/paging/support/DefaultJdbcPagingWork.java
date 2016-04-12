package com.googlecode.easyec.spirit.dao.paging.support;

import com.googlecode.easyec.spirit.dao.paging.*;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.collect;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

/**
 * 默认的JDBC分页工作类
 *
 * @author JunJie
 */
class DefaultJdbcPagingWork implements PagingInterceptor.PagingWork<JdbcPage> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcPagingWork.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String jdbcSql;
    private Class type;

    public DefaultJdbcPagingWork(NamedParameterJdbcTemplate namedParameterJdbcTemplate, String jdbcSql, Class type) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcSql = jdbcSql;
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doPaging(JdbcPage page) throws PagingException, SQLException {
        Map<String, Object> searchTerms = page.getSearchTerms();
        String countSql = page.getPageDialect().getCountSql(jdbcSql);
        logger.debug("Count SQL of JDBC. SQL: [" + countSql + "].");

        Integer totalCount = namedParameterJdbcTemplate.queryForObject(
            countSql, searchTerms, Integer.class
        );
        logger.debug("Total count: [" + totalCount + "].");

        if (totalCount < 1) {
            logger.debug("Cannot found any records. So return it.");

            return;
        }

        String thisSql = jdbcSql;
        List<Sort> sorts = page.getSorts();
        if (isNotEmpty(sorts)) {
            List<String> sortList = new ArrayList<String>(
                collect(sorts, new Transformer() {

                    public Object transform(Object input) {
                        return ((Sort) input).getName() + " " + ((Sort) input).getType();
                    }
                })
            );

            thisSql = page.getPageDialect().getSortedSql(thisSql, sortList);
            logger.debug("Sql for sorting: [" + thisSql + "].");
        }

        String pagedSql = page.getPageDialect().getPagedSql(thisSql);
        logger.debug("Paged SQL of JDBC. SQL: [" + pagedSql + "].");

        int[] pagedParameters = page.getPageDialect().getPagedParameters(
            page.getCurrentPage(), page.getPageSize()
        );

        searchTerms.put("start", pagedParameters[0]);
        searchTerms.put("end", pagedParameters[1]);

        @SuppressWarnings("unchecked")
        List<Object> result = namedParameterJdbcTemplate.query(
            pagedSql, searchTerms, newInstance(type)
        );

        if (page instanceof PageWritable) {
            ((PageWritable) page).setTotalRecordsCount(totalCount);
            ((PageWritable) page).setRecords(result);
        }

        if (page instanceof PageComputable) {
            ((PageComputable) page).compute();
        }
    }
}
