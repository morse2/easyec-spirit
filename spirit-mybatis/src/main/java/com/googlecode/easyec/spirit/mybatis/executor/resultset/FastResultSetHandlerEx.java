package com.googlecode.easyec.spirit.mybatis.executor.resultset;

import com.googlecode.easyec.spirit.mybatis.executor.loader.ResultLoaderEx;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.FastResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 快速的结果集合处理器扩展类类。
 * 该类主要扩展的功能是延迟加载。
 *
 * @author JunJie
 */
public class FastResultSetHandlerEx extends FastResultSetHandler {

    public FastResultSetHandlerEx(Executor executor,
                                  MappedStatement mappedStatement,
                                  ParameterHandler parameterHandler,
                                  ResultHandler resultHandler,
                                  BoundSql boundSql,
                                  RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getNestedQueryConstructorValue(ResultSet rs,
                                                    ResultMapping constructorMapping,
                                                    String columnPrefix
    ) throws SQLException {

        final String nestedQueryId = constructorMapping.getNestedQueryId();
        final MappedStatement nestedQuery = configuration.getMappedStatement(nestedQueryId);
        final Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        final Object nestedQueryParameterObject = prepareParameterForNestedQuery(rs, constructorMapping, nestedQueryParameterType, columnPrefix);
        Object value = null;
        if (nestedQueryParameterObject != null) {
            final BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            final CacheKey key = executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            final Class<?> targetType = constructorMapping.getJavaType();
            final Object nestedQueryCacheObject = getNestedQueryCacheObject(nestedQuery, key);
            if (nestedQueryCacheObject != null && nestedQueryCacheObject instanceof List) {
                value = resultExtractor.extractObjectFromList((List<Object>) nestedQueryCacheObject, targetType);
            } else {
                final ResultLoader resultLoader = new ResultLoaderEx(configuration, executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
                value = resultLoader.loadResult();
            }
        }

        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getNestedQueryMappingValue(ResultSet rs,
                                                MetaObject metaResultObject,
                                                ResultMapping propertyMapping,
                                                ResultLoaderMap lazyLoader,
                                                String columnPrefix
    ) throws SQLException {

        final String nestedQueryId = propertyMapping.getNestedQueryId();
        final String property = propertyMapping.getProperty();
        final MappedStatement nestedQuery = configuration.getMappedStatement(nestedQueryId);
        final Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        final Object nestedQueryParameterObject = prepareParameterForNestedQuery(rs, propertyMapping, nestedQueryParameterType, columnPrefix);
        Object value = OMIT;
        if (nestedQueryParameterObject != null) {
            final BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            final CacheKey key = executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            final Class<?> targetType = propertyMapping.getJavaType();
            final Object nestedQueryCacheObject = getNestedQueryCacheObject(nestedQuery, key);
            if (nestedQueryCacheObject != null && nestedQueryCacheObject instanceof List) {
                value = resultExtractor.extractObjectFromList((List<Object>) nestedQueryCacheObject, targetType);
            } else if (executor.isCached(nestedQuery, key)) {
                executor.deferLoad(nestedQuery, metaResultObject, property, key, targetType);
            } else {
                final ResultLoader resultLoader = new ResultLoaderEx(configuration, executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
                if (configuration.isLazyLoadingEnabled()) {
                    lazyLoader.addLoader(property, metaResultObject, resultLoader);
                } else {
                    value = resultLoader.loadResult();
                }
            }
        }

        return value;
    }

    private Object getNestedQueryCacheObject(MappedStatement nestedQuery, CacheKey key) {
        final Cache nestedQueryCache = nestedQuery.getCache();
        return nestedQueryCache != null ? nestedQueryCache.getObject(key) : null;
    }
}
