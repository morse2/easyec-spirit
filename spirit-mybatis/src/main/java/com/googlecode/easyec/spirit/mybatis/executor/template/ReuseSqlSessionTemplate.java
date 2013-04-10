package com.googlecode.easyec.spirit.mybatis.executor.template;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import static org.apache.ibatis.session.ExecutorType.REUSE;

/**
 * 重用方式操作的<code>SqlSession</code>，
 *
 * @author JunJie
 */
public class ReuseSqlSessionTemplate extends SqlSessionTemplate {

    public ReuseSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory, REUSE);
    }
}
