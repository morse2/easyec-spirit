package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.paging.*;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPage;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPageWritable;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * MyBatis分页拦截器类。
 *
 * @author JunJie
 */
@Aspect
public class MybatisPagingInterceptor implements PagingInterceptor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(MybatisPagingInterceptor.class);

    private SqlSessionFactory sqlSessionFactory;
    private int order;

    /**
     * set方法注入一个{@link SqlSessionFactory}的实例。
     *
     * @param sqlSessionFactory {@link SqlSessionFactory}
     */
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 设置AOP执行的顺序值。
     *
     * @param order 排序值。数值越大，优先权越低
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Around("execution(* com.*..*.dao.*Dao.*(..)) && args(page)")
    public Object intercept(ProceedingJoinPoint joinPoint, Page page) throws Throwable {
        // must be created a new proxy every time.
        MybatisPage mybatisPage = MybatisPageProxy.newProxy(
                page.getPageDialect(),
                page.getCurrentPage(),
                page.getPageSize()
        );

        if (page instanceof MybatisPage && mybatisPage instanceof MybatisPageWritable) {
            ((MybatisPageWritable) mybatisPage).setParameterObject(((MybatisPage) page).getParameterObject());
            ((MybatisPageWritable) mybatisPage).setSorts(((MybatisPage) page).getSorts());
        }

        try {
            Signature signature = joinPoint.getSignature();

            StringBuffer sb = new StringBuffer();
            sb.append(signature.getDeclaringTypeName());
            sb.append(".").append(signature.getName());

            Configuration config = sqlSessionFactory.getConfiguration();
            MappedStatement ms = config.getMappedStatement(sb.toString());

            DefaultResultHandler resultHandler = new DefaultResultHandler();
            new DefaultMybatisPagingWork(ms, resultHandler).doPaging(mybatisPage);

            if (mybatisPage instanceof PageWritable) {
                ((PageWritable) mybatisPage).setRecords(resultHandler.getResultList());
            }

            if (mybatisPage instanceof PageComputable) {
                ((PageComputable) mybatisPage).compute();
            }

            return mybatisPage;
        } catch (PagingException e) {
            logger.error(e.getMessage(), e);

            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new PagingException(e);
        }
    }

    public int getOrder() {
        return this.order;
    }
}
