package com.googlecode.easyec.spirit.mybatis.plugin;

import com.googlecode.easyec.spirit.mybatis.executor.loader.NestedResultSetHandlerEx;
import com.googlecode.easyec.spirit.mybatis.executor.resultset.FastResultSetHandlerEx;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.NestedResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;

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

        if (target instanceof NestedResultSetHandler) {
            return new NestedResultSetHandlerEx(
                    (Executor) FieldUtils.readDeclaredField(target, "executor", true),
                    (MappedStatement) FieldUtils.readDeclaredField(target, "mappedStatement", true),
                    (ParameterHandler) FieldUtils.readDeclaredField(target, "parameterHandler", true),
                    (ResultHandler) FieldUtils.readDeclaredField(target, "resultHandler", true),
                    (BoundSql) FieldUtils.readDeclaredField(target, "boundSql", true),
                    (RowBounds) FieldUtils.readDeclaredField(target, "rowBounds", true)
            ).handleResultSets((Statement) invocation.getArgs()[0]);
        }

        return new FastResultSetHandlerEx(
                (Executor) FieldUtils.readDeclaredField(target, "executor", true),
                (MappedStatement) FieldUtils.readDeclaredField(target, "mappedStatement", true),
                (ParameterHandler) FieldUtils.readDeclaredField(target, "parameterHandler", true),
                (ResultHandler) FieldUtils.readDeclaredField(target, "resultHandler", true),
                (BoundSql) FieldUtils.readDeclaredField(target, "boundSql", true),
                (RowBounds) FieldUtils.readDeclaredField(target, "rowBounds", true)
        ).handleResultSets((Statement) invocation.getArgs()[0]);
    }
}
