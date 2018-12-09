package com.googlecode.easyec.spirit.mybatis.mapper.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.SmartFactoryBean;

import java.lang.reflect.Proxy;

public class DaoMapperFactoryBean<T> implements SmartFactoryBean<T> {

    private Class<T> targetClass;
    private SqlSessionFactory sqlSessionFactory;

    public DaoMapperFactoryBean() { }

    public DaoMapperFactoryBean(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public T getObject() throws Exception {
        return targetClass.cast(
            Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                new Class[] { targetClass },
                new DaoMapperProxy(targetClass, sqlSessionFactory)
            )
        );
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }
}
