package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.PageWritable;
import com.googlecode.easyec.spirit.dao.paging.PagingException;
import com.googlecode.easyec.spirit.dao.paging.PagingInterceptor;
import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import com.googlecode.easyec.spirit.mybatis.mapper.support.DaoMapperUtils;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPage;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPageWritable;
import org.apache.commons.lang3.ArrayUtils;
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
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Around("execution(* *..*.*Dao.*(..)) && args(page)")
    public Object intercept(ProceedingJoinPoint joinPoint, Page page) throws Throwable {
        // Since 0.6.4.1
        if (!(page instanceof MybatisPage)) {
            logger.debug("The Page object isn't instanceof MybatisPage, so ignore.");

            return joinPoint.proceed(
                joinPoint.getArgs()
            );
        }

        // must be created a new proxy every time.
        MybatisPage mybatisPage = MybatisPageProxy.newProxy(
            page.getPageDialect(),
            page.getCurrentPage(),
            page.getPageSize()
        );

        if (mybatisPage instanceof MybatisPageWritable) {
            ((MybatisPageWritable) mybatisPage).setParameterObject(((MybatisPage) page).getParameterObject());
            ((MybatisPageWritable) mybatisPage).setSorts(((MybatisPage) page).getSorts());
        }

        try {
            List<String> mappedStatementIds = new ArrayList<String>();

            // 获取方法签名
            Signature signature = joinPoint.getSignature();
            // 获取MyBatis的配置信息
            Configuration config = sqlSessionFactory.getConfiguration();
            MappedStatement ms = null;

            boolean b = DelegateDao.class.getName().equals(signature.getDeclaringTypeName());
            logger.debug("Is DAO instance of class DelegateDao? [{}]", b);

            if (b) {
                Class[] interfaces = ClassUtils.getAllInterfaces(joinPoint.getTarget());
                if (ArrayUtils.isNotEmpty(interfaces)) {
                    for (Class cls : interfaces) {
                        if (DelegateDao.class.isAssignableFrom(cls)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(cls.getName()).append(".");
                            sb.append(signature.getName());
                            logger.debug("Mapped statement id in proxy interface is: [{}].", sb);

                            mappedStatementIds.add(sb.toString());
                        }
                    }
                }
            }

            StringBuffer sb = new StringBuffer();
            // 如果是DaoMapper代理，则使用实际的域模型类作为mybatis的命名空间
            if (DaoMapperUtils.isProxy(joinPoint.getTarget())) {
                Class<?> cls = DaoMapperUtils.getDomainModelClass(joinPoint.getTarget());
                if (cls != null) sb.append(cls.getName());
            }

            if (sb.length() == 0) {
                sb.append(signature.getDeclaringTypeName());
            }

            sb.append(".").append(signature.getName());
            logger.debug("Original mapped statement id is: [{}].", sb);

            mappedStatementIds.add(sb.toString());

            boolean continued = true;
            for (int i = 0; continued && i < mappedStatementIds.size(); i++) {
                try {
                    ms = config.getMappedStatement(mappedStatementIds.get(i));
                    continued = (null == ms);
                } catch (Exception e) {
                    // 如果报错，则说明没找到MappedStatement配置信息，
                    // 需要从代理类的接口中查找签名信息，
                    // 以获取正确的MappedStatement配置信息
                    logger.trace(e.getMessage(), e);
                }
            }

            Assert.notNull(ms, "No MappedStatement object was found in MyBatis configuration.");

            DefaultResultHandler resultHandler = new DefaultResultHandler();
            new DefaultMybatisPagingWork(ms, resultHandler).doPaging(mybatisPage);

            if (mybatisPage instanceof PageWritable) {
                ((PageWritable) mybatisPage).setRecords(resultHandler.getResultList());
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
