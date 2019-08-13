package com.googlecode.easyec.spirit.mybatis.executor.template;

import com.googlecode.easyec.spirit.dao.utils.DaoUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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

        return _doBatchUpdate(statement, DaoUtils.convertToCollection(parameter));
    }

    @Override
    public int update(String statement, Object parameter) {
        if (null == parameter) return update(statement);

        return _doBatchUpdate(statement, DaoUtils.convertToCollection(parameter));
    }

    @Override
    public int delete(String statement, Object parameter) {
        if (null == parameter) return delete(statement);

        return _doBatchUpdate(statement, DaoUtils.convertToCollection(parameter));
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
}
