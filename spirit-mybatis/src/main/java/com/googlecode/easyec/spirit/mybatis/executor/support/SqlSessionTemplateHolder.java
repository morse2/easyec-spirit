package com.googlecode.easyec.spirit.mybatis.executor.support;

import com.googlecode.easyec.spirit.mybatis.executor.template.BatchSqlSessionTemplate;
import com.googlecode.easyec.spirit.mybatis.executor.template.ReuseSqlSessionTemplate;
import com.googlecode.easyec.spirit.mybatis.executor.template.SimpleSqlSessionTemplate;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.Assert;

/**
 * <code>SqlSessionTemplate</code>的holder类。
 * 该类在当前事务线程中决定了使用的<code>SqlSessionTemplate</code>
 * 是哪个。
 *
 * @author JunJie
 * @see org.mybatis.spring.SqlSessionTemplate
 */
public final class SqlSessionTemplateHolder {

    private static final ThreadLocal<SqlSessionTemplate> _t = new ThreadLocal<SqlSessionTemplate>();

    private SqlSessionTemplateHolder() {
        // no op
    }

    /**
     * 从当前线程中获取{@link SqlSessionTemplate}对象的实例。
     * <p>此对象实例依赖于{@link SqlSessionTemplateDecisionInterceptor}的判断</p>
     *
     * @return <code>SqlSessionTemplate</code>对象
     */
    public static SqlSessionTemplate get() {
        return _t.get();
    }

    /**
     * 为当前线程设置一个<code>SqlSessionTemplate</code>的实例。
     * <p>在调用此方法后，只要在此线程中的应用，都可以通过{@link #get()}方法来获取<code>SqlSessionTemplate</code>的实例。</p>
     *
     * @param sqlSessionTemplate {@link SqlSessionTemplate}对象
     * @see SimpleSqlSessionTemplate
     * @see ReuseSqlSessionTemplate
     * @see BatchSqlSessionTemplate
     */
    public static void set(SqlSessionTemplate sqlSessionTemplate) {
        Assert.notNull(sqlSessionTemplate, "SqlSessionTemplate object is null.");
        _t.set(sqlSessionTemplate);
    }

    /**
     * 从当前线程中删除<code>SqlSessionTemplate</code>的实例对象。
     * <p>注意：调用此方法后，方法{@link #get()}则返回null</p>
     */
    public static void remove() {
        _t.set(null);
    }
}
