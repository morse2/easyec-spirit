package com.googlecode.easyec.spirit.mybatis.executor.support;

import com.googlecode.easyec.spirit.mybatis.executor.annotation.Batch;
import com.googlecode.easyec.spirit.mybatis.executor.template.BatchSqlSessionTemplate;
import com.googlecode.easyec.spirit.mybatis.executor.template.ReuseSqlSessionTemplate;
import com.googlecode.easyec.spirit.mybatis.executor.template.SimpleSqlSessionTemplate;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

import static com.googlecode.easyec.spirit.mybatis.executor.support.SqlSessionTemplateHolder.*;

/**
 * <code>SqlSessionTemplate</code>决定拦截器类。
 * 该拦截器提供批处理与简单处理两种方式的SQL操作。
 * 当类的方法使用了{@link Batch}注解来标注此方法是批处理方法时，
 * 则该方法内部的所有SQL操作都将视为批处理操作。
 * 反之，则认为是简单的SQL操作。<b>注意</b>，
 * 注解类Batch必须被标注在业务方法上，即事务的起点。
 *
 * @author JunJie
 * @see Batch
 * @see SqlSessionTemplateExecutor
 */
@Aspect
public class SqlSessionTemplateDecisionInterceptor implements Ordered {

    private boolean executeWithinBatch = true;
    private SqlSessionFactory sqlSessionFactory;
    private int order;

    /**
     * 设置{@link Batch}注解标记是否启用。
     * <p>默认开启批处理功能。</p>
     *
     * @param executeWithinBatch 真或假；真开启批处理功能，假关闭批处理
     */
    public void setExecuteWithinBatch(boolean executeWithinBatch) {
        this.executeWithinBatch = executeWithinBatch;
    }

    /**
     * 设置{@link SqlSessionFactory}对象实例。
     *
     * @param sqlSessionFactory <code>SqlSessionFactory</code>对象
     */
    @Autowired
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

    /**
     * AOP拦截器拦截方法。
     *
     * @param joinPoint 切入点
     * @return 结果对象
     * @throws Throwable
     */
    @Around("execution(* com.*..*.service.*Service.*(..))")
    public Object decide(ProceedingJoinPoint joinPoint) throws Throwable {
        // 如果当前线程中已经存在SqlSessionTemplate对象的话，
        // 则直接使用此对象访问数据源
        if (null != get()) return joinPoint.proceed(joinPoint.getArgs());

        try {
            if (executeWithinBatch) {
                Object target = joinPoint.getTarget();

                Class<?> targetClass = AopUtils.getTargetClass(target);
                if (null == targetClass) {
                    targetClass = target.getClass();
                }

                // 强转成方法签名类
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = AopUtils.getMostSpecificMethod(signature.getMethod(), targetClass);
                if (null == method) {
                    method = signature.getMethod();
                }

                Batch batch = method.getAnnotation(Batch.class);
                if (null != batch) {
                    createSqlSessionTemplate(true);
                } else {
                    createSqlSessionTemplate(false);
                }
            } else {
                createSqlSessionTemplate(false);
            }

            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            remove();
        }
    }

    private void createSqlSessionTemplate(boolean executeWithinBatch) {
        if (executeWithinBatch) {
            set(new BatchSqlSessionTemplate(sqlSessionFactory));
        } else {
            ExecutorType executorType = sqlSessionFactory.getConfiguration().getDefaultExecutorType();
            switch (executorType) {
                case SIMPLE:
                    set(new SimpleSqlSessionTemplate(sqlSessionFactory));
                    break;
                case REUSE:
                    set(new ReuseSqlSessionTemplate(sqlSessionFactory));
                    break;
                case BATCH:
                    set(new BatchSqlSessionTemplate(sqlSessionFactory));
                    break;
                default:
                    throw new IllegalStateException("Unknown default executor type in MyBatis configuration file.");
            }
        }
    }

    public int getOrder() {
        return this.order;
    }
}
