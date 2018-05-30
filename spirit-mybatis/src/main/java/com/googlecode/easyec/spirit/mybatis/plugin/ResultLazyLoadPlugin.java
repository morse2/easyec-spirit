package com.googlecode.easyec.spirit.mybatis.plugin;

import com.googlecode.easyec.spirit.mybatis.plugin.support.TransactionalContextHolder;
import com.googlecode.easyec.spirit.web.utils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * 结果集延迟加载的补充插件类。
 * 该类主要为Mybatis的延迟加载对象
 * 提供了超出事务范围的延迟加载对象的功能支持。
 *
 * @author JunJie
 */
@Intercepts(
    @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = { Connection.class, Integer.class }
    )
)
public class ResultLazyLoadPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Connection conn = (Connection) invocation.getArgs()[0];
        if (conn != null && !conn.isClosed()) {
            return invocation.proceed();
        }

        Object target = invocation.getTarget();
        if (!(target instanceof StatementHandler)) {
            return invocation.proceed();
        }

        StatementHandler handler = (StatementHandler) target;
        if (handler instanceof RoutingStatementHandler) {
            handler = BeanUtils.readField(handler, "delegate", StatementHandler.class);
        }

        Configuration _conf = BeanUtils.readField(handler, "configuration", Configuration.class);

        Transaction _tx = null;
        if (!TransactionalContextHolder.has()) {
            Executor executor = newExecutor(_conf);
            BeanUtils.writeField(handler, "executor", executor);

            _tx = executor.getTransaction();
            TransactionalContextHolder.set(_tx);
        }

        Assert.notNull(_tx, "Transaction mustn't be null.");

        return new Invocation(
            target, invocation.getMethod(),
            new Object[] {
                _tx.getConnection(),
                invocation.getArgs()[1]
            }).proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // no op
    }

    private Executor newExecutor(Configuration configuration) {
        final Environment environment = configuration.getEnvironment();
        if (environment == null)
            throw new ExecutorException("ResultLoader could not load lazily.  Environment was not configured.");
        final DataSource ds = environment.getDataSource();
        if (ds == null)
            throw new ExecutorException("ResultLoader could not load lazily.  DataSource was not configured.");
        final TransactionFactory transactionFactory = environment.getTransactionFactory();
        final Transaction tx = transactionFactory.newTransaction(ds, null, false);
        return configuration.newExecutor(tx, configuration.getDefaultExecutorType());
    }
}
