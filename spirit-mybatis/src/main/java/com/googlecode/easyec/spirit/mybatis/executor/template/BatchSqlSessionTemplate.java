package com.googlecode.easyec.spirit.mybatis.executor.template;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.util.*;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.ibatis.session.ExecutorType.BATCH;

/**
 * 支持批处理的<code>SqlSession</code>
 *
 * @author JunJie
 */
public class BatchSqlSessionTemplate extends SqlSessionTemplate {

    private static final Logger logger = LoggerFactory.getLogger(BatchSqlSessionTemplate.class);

    public BatchSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, BATCH);
    }

    @Override
    public int insert(String statement, Object parameter) {
        if (null == parameter) return insert(statement);

        return doBatch(statement, convertToCollection(parameter));
    }

    @Override
    public int update(String statement, Object parameter) {
        if (null == parameter) return update(statement);

        return doBatch(statement, convertToCollection(parameter));
    }

    @Override
    public int delete(String statement, Object parameter) {
        if (null == parameter) return delete(statement);

        return doBatch(statement, convertToCollection(parameter));
    }

    private int doBatch(String statement, Collection items) {
        int value = 0;

        if (isNotEmpty(items)) {
            for (Object o : items) {
                super.update(statement, o);
            }

            List<BatchResult> results = flushStatements();

            if (results.isEmpty()) {
                throw new InvalidDataAccessResourceUsageException("Batch execution returned invalid results. " +
                        "BatchResult objects return one row at least.");
            }

            int size = results.size();
            logger.debug("Current batch size is: [" + size + "].");

            int[] updateCounts = results.get(size - 1).getUpdateCounts();

            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] == 0) {
                    logger.warn("Row line: [" + (i + 1) + "] has no effect. update count: [" + updateCounts[i] + "].");
                }

                value += updateCounts[i];
            }
        }

        return value;
    }

    private Collection convertToCollection(Object o) {
        if (o instanceof Collection) {
            return (Collection) o;
        }

        if (o.getClass().isArray()) {
            return new ArrayList<Object>(Arrays.asList((Object[]) o));
        }

        if (o instanceof Map && ((Map) o).containsKey("list")) {
            return (Collection) ((Map) o).get("list");
        }

        if (o instanceof Map && ((Map) o).containsKey("array")) {
            return new ArrayList<Object>(Arrays.asList(((Map) o).get("array")));
        }

        return new ArrayList<Object>(Arrays.asList(o));
    }
}
