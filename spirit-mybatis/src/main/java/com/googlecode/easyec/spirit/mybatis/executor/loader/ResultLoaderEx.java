package com.googlecode.easyec.spirit.mybatis.executor.loader;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Mybatis延迟加载属性结果加载器的扩展类。
 * 该类主要为延迟加载父类进行扩展，
 * 目的在于解决数据库连接被关闭的问题。
 *
 * @author JunJie
 */
public class ResultLoaderEx extends ResultLoader {

    public ResultLoaderEx(Configuration config,
                          Executor executor,
                          MappedStatement mappedStatement,
                          Object parameterObject,
                          Class<?> targetType,
                          CacheKey cacheKey,
                          BoundSql boundSql) {
        super(config, executor, mappedStatement, parameterObject, targetType, cacheKey, boundSql);
    }

    public Object loadResult() throws SQLException {
        List<Object> list = selectList();
        resultObject = resultExtractor.extractObjectFromList(list, targetType);
        return resultObject;
    }

    private <E> List<E> selectList() throws SQLException {
        Executor localExecutor = executor;
        // 如果执行器被关闭了，则创建一个新的执行器实例
        if (localExecutor.isClosed()) {
            localExecutor = newExecutor();
        } else {
            // 检查当前执行器中的数据库链接，如果关闭了，则创建一个新的执行器实例
            Connection conn = localExecutor.getTransaction().getConnection();
            if (null == conn || conn.isClosed()) {
                localExecutor = newExecutor();
            }
        }

        try {
            return localExecutor.query(mappedStatement, parameterObject, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, cacheKey, boundSql);
        } finally {
            if (localExecutor != executor) {
                localExecutor.close(false);
            }
        }
    }

    private Executor newExecutor() throws SQLException {
        final Environment environment = configuration.getEnvironment();
        if (environment == null)
            throw new ExecutorException("ResultLoader could not load lazily.  Environment was not configured.");
        final DataSource ds = environment.getDataSource();
        if (ds == null)
            throw new ExecutorException("ResultLoader could not load lazily.  DataSource was not configured.");
        final TransactionFactory transactionFactory = environment.getTransactionFactory();
        final Transaction tx = transactionFactory.newTransaction(ds, null, false);
        return configuration.newExecutor(tx, ExecutorType.SIMPLE);
    }
}
