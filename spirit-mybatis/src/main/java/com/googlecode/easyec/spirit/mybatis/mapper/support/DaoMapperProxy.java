package com.googlecode.easyec.spirit.mybatis.mapper.support;

import com.googlecode.easyec.spirit.mybatis.executor.support.SqlSessionTemplateHolder;
import com.googlecode.easyec.spirit.mybatis.mapper.annotation.Mapper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.springframework.beans.BeanUtils.instantiateClass;

public class DaoMapperProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(DaoMapperProxy.class);

    private Class<?> mapperClass;
    private SqlSessionFactory sqlSessionFactory;

    public DaoMapperProxy(Class<?> mapperClass, SqlSessionFactory sqlSessionFactory) {
        this.mapperClass = mapperClass;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("createNew".equals(method.getName())) {
            return _doCreateNew();
        }

        if ("toString".equals(method.getName())) {
            return this.toString();
        }

        return new DaoMapperMethod(
            getDomainModelClass(), method, sqlSessionFactory.getConfiguration()
        ).execute(getSqlSessionTemplate(), args);
    }

    public Class<?> getDomainModelClass() throws ClassNotFoundException {
        Mapper mapper = mapperClass.getAnnotation(Mapper.class);
        return ClassUtils.forName(
            mapper.domainModel(),
            ClassUtils.getDefaultClassLoader()
        );
    }

    @SuppressWarnings("unchecked")
    private Object _doCreateNew() throws ClassNotFoundException {
        Class domainModelType = getDomainModelClass();
        if (domainModelType == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("Cannot resolve generic type 'DomainModel'. Class: [")
                .append(mapperClass.getName())
                .append("].");

            logger.error(sb.toString());

            throw new IllegalStateException(sb.toString());
        }

        Configuration configuration = sqlSessionFactory.getConfiguration();
        TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        Class<Object> alias = typeAliasRegistry.resolveAlias(domainModelType.getCanonicalName());
        if (alias == null) {
            typeAliasRegistry.registerAlias(
                domainModelType.getCanonicalName(),
                domainModelType
            );

            alias = domainModelType;
        }

        return instantiateClass(alias);
    }

    private SqlSessionTemplate getSqlSessionTemplate() {
        SqlSessionTemplate sqlSessionTemplate = SqlSessionTemplateHolder.get();

        if (sqlSessionTemplate == null) {

        }

        return sqlSessionTemplate;
    }
}
