package com.googlecode.easyec.spirit.mybatis.executor.support;

import com.googlecode.easyec.spirit.mybatis.mapper.DelegateDao;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * <code>SqlSessionTemplate</code>执行器。
 * 此类必须依赖于<code>SqlSessionTemplateDecisionInterceptor</code>类，
 * 来决定使用哪个类型的<code>SqlSessionTemplate</code>实例。
 *
 * @author JunJie
 */
@Aspect
public class SqlSessionTemplateExecutor implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SqlSessionTemplateExecutor.class);
    private int order;

    public int getOrder() {
        return order;
    }

    /**
     * 设置AOP执行的顺序值。
     *
     * @param order 排序值。数值越大，优先权越低
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Around("execution(* com.*..*.dao.*Dao.*(..))")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        SqlSessionTemplate sqlSessionTemplate = SqlSessionTemplateHolder.get();

        if (null == sqlSessionTemplate) {
            throw new UnsupportedOperationException("No SqlSessionTemplate was set. " +
                "You must configurer SQlSessionTemplateDecisionInterceptor class firstly.");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        if (DelegateDao.class.getName().equals(signature.getDeclaringTypeName())) {
            Class[] interfaces = ClassUtils.getAllInterfaces(joinPoint.getTarget());
            if (ArrayUtils.isNotEmpty(interfaces)) {
                SqlSessionNestedException ex = new SqlSessionNestedException();

                for (Class cls : interfaces) {
                    if (DelegateDao.class.isAssignableFrom(cls)) {
                        try {
                            return new MapperMethod(
                                cls,
                                signature.getMethod(),
                                sqlSessionTemplate.getConfiguration()
                            ).execute(sqlSessionTemplate, joinPoint.getArgs());
                        } catch (Exception e) {
                            ex.addException(e);
                        }
                    }
                }

                if (ex.hasExceptions()) {
                    logger.warn("Some method was invoked by DelegateDao object, " +
                        "but some errors were occurred. Please catch this exception and " +
                        "check the log in trace level.");

                    List<Exception> exceptions = ex.getLinkedExceptions();
                    for (Exception e : exceptions) {
                        logger.trace(e.getMessage(), e);
                    }

                    throw ex;
                }
            }
        }

        return new MapperMethod(
            signature.getDeclaringType(),
            signature.getMethod(),
            sqlSessionTemplate.getConfiguration()
        ).execute(sqlSessionTemplate, joinPoint.getArgs());
    }
}
