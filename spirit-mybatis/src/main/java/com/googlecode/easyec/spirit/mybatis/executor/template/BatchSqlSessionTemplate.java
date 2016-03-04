package com.googlecode.easyec.spirit.mybatis.executor.template;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.ibatis.executor.BatchExecutor.BATCH_UPDATE_RETURN_VALUE;
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

        return _doBatchUpdate(statement, _convertToCollection(parameter));
    }

    @Override
    public int update(String statement, Object parameter) {
        if (null == parameter) return update(statement);

        return _doBatchUpdate(statement, _convertToCollection(parameter));
    }

    @Override
    public int delete(String statement, Object parameter) {
        if (null == parameter) return delete(statement);

        return _doBatchUpdate(statement, _convertToCollection(parameter));
    }

    /* 执行批处理更新的方法 */
    private int _doBatchUpdate(String statement, Collection items) {
        if (isNotEmpty(items)) {
            for (Object o : items) {
                logger.debug("Prepare to update with batch mode. Statement: [{}].", statement);

                super.update(statement, o);
            }
        }

        return BATCH_UPDATE_RETURN_VALUE;
    }

    /* 将参数对象转换为必要的集合类型对象 */
    private Collection _convertToCollection(Object o) {
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
