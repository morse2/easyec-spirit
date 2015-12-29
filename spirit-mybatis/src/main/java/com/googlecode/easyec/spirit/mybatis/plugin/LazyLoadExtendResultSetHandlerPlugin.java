package com.googlecode.easyec.spirit.mybatis.plugin;

import com.googlecode.easyec.spirit.mybatis.executor.resultset.DefaultResultSetHandlerEx;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;

import static com.googlecode.easyec.spirit.web.utils.BeanUtils.readField;

/**
 * 结果集合处理器插件类。
 * 此类负责扩展处理延迟加载结果集。
 *
 * @author JunJie
 */
@Intercepts({
    @Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = { Statement.class })
})
public class LazyLoadExtendResultSetHandlerPlugin extends PluginAdapter {

    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        if (target instanceof DefaultResultSetHandler) {
            return new DefaultResultSetHandlerEx(
                readField(target, "executor", Executor.class),
                readField(target, "mappedStatement", MappedStatement.class),
                readField(target, "parameterHandler", ParameterHandler.class),
                readField(target, "resultHandler", ResultHandler.class),
                readField(target, "boundSql", BoundSql.class),
                readField(target, "rowBounds", RowBounds.class)
            ).handleResultSets((Statement) invocation.getArgs()[0]);
        }

        return target;
    }
}
