package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.PageComputable;
import com.googlecode.easyec.spirit.dao.paging.PageWritable;
import com.googlecode.easyec.spirit.dao.paging.PagingException;
import com.googlecode.easyec.spirit.dao.paging.PagingInterceptor;
import com.googlecode.easyec.spirit.mybatis.executor.result.NumberResultHandler;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPage;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import org.apache.commons.collections4.Transformer;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.collect;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 默认的MyBatis分页工作类。
 *
 * @author JunJie
 */
class DefaultMybatisPagingWork implements PagingInterceptor.PagingWork<MybatisPage> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMybatisPagingWork.class);
    public static final String SELECT_COUNT_KEY_SUFFIX = "!selectCountKey";
    public static final String RESULT_MAP_KEY_SUFFIX = "!resultMapKey";
    public static final String SELECT_CLONE_KEY_SUFFIX = "!selectCloneKey";

    private MappedStatement mappedStatement;
    private ResultHandler resultHandler;
    private Transaction transaction;

    public DefaultMybatisPagingWork(MappedStatement mappedStatement, ResultHandler resultHandler) {
        this.mappedStatement = mappedStatement;
        this.resultHandler = resultHandler;
        init();
    }

    private void init() {
        if (mappedStatement == null) {
            throw new IllegalArgumentException("MappedStatement object cannot be null.");
        }

        if (resultHandler == null) {
            throw new IllegalArgumentException("ResultHandler object cannot be null.");
        }

        DataSource dataSource = mappedStatement.getConfiguration().getEnvironment().getDataSource();
        this.transaction = new SpringManagedTransactionFactory().newTransaction(dataSource, null, false);
    }

    @SuppressWarnings("unchecked")
    private void mergeParameterObject(Map<String, Object> parameterMap, Object parameterObject) {
        if (parameterObject != null) {
            if (parameterObject instanceof Map) {
                parameterMap.putAll((Map) parameterObject);
            } else {
                BeanWrapperImpl bw = new BeanWrapperImpl(parameterObject);
                PropertyDescriptor[] propertyDescriptors = bw.getPropertyDescriptors();
                for (PropertyDescriptor pd : propertyDescriptors) {
                    String name = pd.getName();

                    if (bw.isReadableProperty(name)) {
                        Object v = bw.getPropertyValue(name);
                        if (v != null) {
                            parameterMap.put(name, v);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void doPaging(MybatisPage page) throws PagingException, SQLException {
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        if (!SqlCommandType.SELECT.equals(sqlCommandType)) {
            StringBuilder msg = new StringBuilder();
            msg.append("Sql command type isn't SELECT, so ignore doing page. Sql command type: [");
            msg.append(sqlCommandType).append("].");
            logger.warn(msg.toString());

            throw new PagingException(msg.toString());
        }

        Object parameterObject = page.getParameterObject();
        logger.debug("Parameter object is null? [" + (parameterObject == null) + "].");

        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        logger.debug("Bound sql: [" + boundSql.getSql() + "].");

        Configuration config = mappedStatement.getConfiguration();

        String selectCountKey = mappedStatement.getId() + SELECT_COUNT_KEY_SUFFIX;
        PageDialect pageDialect = page.getPageDialect();
        logger.debug("PageDialect's name: [" + pageDialect.getDialectName() + "].");

        String countSql = pageDialect.getCountSql(boundSql.getSql());
        logger.debug("This count sql: [" + countSql + "].");

        SqlSource sqlSource = new PageDialectSqlSource(config, countSql, mappedStatement.getSqlSource(), boundSql.getParameterMappings());

        logger.debug("build result map object. key: [" + (selectCountKey + RESULT_MAP_KEY_SUFFIX) + "].");

        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(new ResultMap.Builder(
            config,
            selectCountKey + RESULT_MAP_KEY_SUFFIX,
            Integer.class,
            new ArrayList<ResultMapping>(0)
        ).build());

        logger.debug("build count sql MappedStatement object. key: [" + selectCountKey + "].");

        MappedStatement selectCountMS = new MappedStatement.Builder(
            config,
            selectCountKey,
            sqlSource,
            SqlCommandType.SELECT)
            .keyGenerator(mappedStatement.getKeyGenerator())
            .cache(mappedStatement.getCache())
            .useCache(mappedStatement.isUseCache())
            .parameterMap(mappedStatement.getParameterMap())
            .resultMaps(resultMaps)
            .timeout(mappedStatement.getTimeout())
            .build();

        // set as current transaction
        Executor selectCountExecutor = config.newExecutor(transaction);

        NumberResultHandler handler = new NumberResultHandler();
        selectCountExecutor.query(selectCountMS, parameterObject, RowBounds.DEFAULT, handler);

        logger.info("Total record count: [" + handler.getTotalRecordCount() + "].");

        if (page instanceof PageWritable) {
            ((PageWritable) page).setTotalRecordsCount(handler.getTotalRecordCount());
        }

        // 固化分页结果
        if (page instanceof PageComputable) {
            ((PageComputable) page).compute();
        }

        /*
         * 新增逻辑，如果计算总页数小于1，
         * 则认为当前分页查询是没有任何结果
         * 可以返回，因此余下的逻辑是没有必要
         * 执行的。Since 0.6.0
         */
        if (page.getTotalRecordsCount() < 1) {
            logger.info("The total record count is less than 1, so there is no need to do logic else.");

            return;
        }

        /*
         * 新增逻辑，如果当前传递的页码大于
         * 分页语句执行出来的总页码的时候，
         * 则不执行余下的逻辑操作。
         * Since 0.6.0
         */
        if (page.getCurrentPage() > page.getTotalSize()) {
            logger.warn("The current page is greater than size of total pages, so there is no need to do logic else.");

            return;
        }

        String thisSql = boundSql.getSql();
        logger.debug("This sql: [{}]", thisSql);

        List<Sort> sorts = page.getSorts();
        if (isNotEmpty(sorts)) {
            List<String> list = new ArrayList<String>(
                collect(sorts, new Transformer() {

                    public Object transform(Object input) {
                        return ((Sort) input).getName() + " " + ((Sort) input).getType();
                    }
                })
            );

            thisSql = pageDialect.getSortedSql(thisSql, list);
            logger.debug("Sql for sorting: [" + thisSql + "].");
        }

        thisSql = pageDialect.getPagedSql(thisSql);
        logger.debug("Paged sql: [{}].", thisSql);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

        parameterMappings.addAll(boundSql.getParameterMappings());
        mergeParameterObject(parameterMap, parameterObject);

        int[] pagedParameters = pageDialect.getPagedParameters(page.getCurrentPage(), page.getPageSize());
        if (pagedParameters != null) {
            for (int i = 0; i < pagedParameters.length; i++) {
                logger.debug("Paged parameter, index [" + i + "], value [" + pagedParameters[i] + "].");

                ParameterMapping mapping = new ParameterMapping.Builder(
                    config,
                    "pagedParameter" + i,
                    Integer.class)
                    .typeHandler(new IntegerTypeHandler())
                    .jdbcType(JdbcType.INTEGER).build();

                parameterMappings.add(mapping);
                parameterMap.put("pagedParameter" + i, pagedParameters[i]);
            }
        }

        String selectCloneKey = mappedStatement.getId() + SELECT_CLONE_KEY_SUFFIX;
        SqlSource pagedSqlSource = new PageDialectSqlSource(config, thisSql, mappedStatement.getSqlSource(), parameterMappings);
        MappedStatement pagedMappedStatement = new MappedStatement.Builder(
            config,
            selectCloneKey,
            pagedSqlSource,
            SqlCommandType.SELECT)
            .keyGenerator(mappedStatement.getKeyGenerator())
            .cache(mappedStatement.getCache())
            .useCache(mappedStatement.isUseCache())
            .parameterMap(new ParameterMap.Builder(config,
                mappedStatement.getParameterMap().getId(),
                mappedStatement.getParameterMap().getType(),
                parameterMappings).build())
            .resultMaps(mappedStatement.getResultMaps())
            .resultSetType(mappedStatement.getResultSetType())
            .timeout(mappedStatement.getTimeout())
            .build();

        Executor pagedQueryExecutor = config.newExecutor(transaction);
        pagedQueryExecutor.query(pagedMappedStatement, parameterMap, RowBounds.DEFAULT, resultHandler);
    }
}
