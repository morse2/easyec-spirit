package com.googlecode.easyec.spirit.mybatis.paging.support;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 分页支持的<code>SqlSource</code>实现类。
 *
 * @author JunJie
 */
class PageDialectSqlSource implements SqlSource {

    private String sql;
    private SqlSource sqlSource;
    private Configuration configuration;
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    PageDialectSqlSource(Configuration configuration, String sql, SqlSource sqlSource, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.sqlSource = sqlSource;
        this.configuration = configuration;
        this.parameterMappings.addAll(parameterMappings);
    }

    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql thisBoundSql = sqlSource.getBoundSql(parameterObject);

        BoundSql newBoundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
        ReflectionUtils.doWithFields(
                thisBoundSql.getClass(),
                new DefaultFieldCallback(thisBoundSql, newBoundSql),
                new DefaultFieldFilter()
        );

        return newBoundSql;
    }

    private class DefaultFieldCallback implements ReflectionUtils.FieldCallback {

        private BoundSql thisBoundSql;
        private BoundSql newBoundSql;

        private DefaultFieldCallback(BoundSql thisBoundSql, BoundSql newBoundSql) {
            this.thisBoundSql = thisBoundSql;
            this.newBoundSql = newBoundSql;
        }

        @SuppressWarnings("unchecked")
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            if (!field.isAccessible()) field.setAccessible(true);

            Map<String, Object> map = (Map<String, Object>) field.get(thisBoundSql);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                newBoundSql.setAdditionalParameter(key, map.get(key));
            }
        }
    }

    private class DefaultFieldFilter implements ReflectionUtils.FieldFilter {

        public boolean matches(Field field) {
            return null != field && "additionalParameters".equals(field.getName());
        }
    }
}
