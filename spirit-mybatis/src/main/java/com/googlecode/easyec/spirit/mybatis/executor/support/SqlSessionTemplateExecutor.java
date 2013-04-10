package com.googlecode.easyec.spirit.mybatis.executor.support;

import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.Ordered;

/**
 * <code>SqlSessionTemplate</code>执行器。
 * 此类必须依赖于<code>SqlSessionTemplateDecisionInterceptor</code>类，
 * 来决定使用哪个类型的<code>SqlSessionTemplate</code>实例。
 *
 * @author JunJie
 */
@Aspect
public class SqlSessionTemplateExecutor implements Ordered {

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

        return new MapperMethod(
                signature.getDeclaringType(),
                signature.getMethod(),
                sqlSessionTemplate.getConfiguration()
        ).execute(sqlSessionTemplate, joinPoint.getArgs());
    }
}
