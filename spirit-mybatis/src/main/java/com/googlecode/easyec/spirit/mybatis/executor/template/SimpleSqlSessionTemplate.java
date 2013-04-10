package com.googlecode.easyec.spirit.mybatis.executor.template;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import static org.apache.ibatis.session.ExecutorType.SIMPLE;

/**
 * 简单操作的<code>SqlSession</code>，
 * 默认则以此方式执行SQL语句。
 *
 * @author JunJie
 */
public class SimpleSqlSessionTemplate extends SqlSessionTemplate {

    public SimpleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, SIMPLE);
    }
}
