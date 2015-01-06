package com.googlecode.easyec.spirit.dao.id.support;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * 基于Spring事务平台的主键生成决定拦截器类。
 * <p/>
 * Spring平台的事务管理机制来控制主键生成机制的同步操作
 * </p>
 *
 * @author JunJie
 */
@Aspect
public class PlatformSequenceGenerateDecisionInterceptor extends AbstractSequenceGenerateDecisionInterceptor {

    private PlatformTransactionManager transactionManager;

    // transactional isolation level
    private int isolationLevel = ISOLATION_READ_COMMITTED;
    // transactional propagation behavior
    private int propagationBehavior = PROPAGATION_REQUIRES_NEW;

    @Override
    @Before(before_expr)
    public void before(JoinPoint joinPoint, final Object o) throws Exception {
        TransactionTemplate t = new TransactionTemplate(transactionManager);
        t.setPropagationBehavior(propagationBehavior);
        t.setIsolationLevel(isolationLevel);

        t.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    logger.debug("Do identifier generation.");
                    sequenceGenerator.process(o);
                    logger.debug("Finish identifier generation.");
                } catch (Exception e) {
                    logger.trace(e.getMessage(), e);

                    // 修改事务回滚方式，抛出事务系统异常，让框架帮助回滚事务
                    throw new TransactionSystemException(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 返回主键织入事务的隔离级别
     *
     * @return <code>TransactionDefinition</code>定义
     * @see org.springframework.transaction.TransactionDefinition
     */
    public int getIsolationLevel() {
        return isolationLevel;
    }

    /**
     * 设置当前主键织入事务的隔离级别
     *
     * @param isolationLevel <code>TransactionDefinition</code>定义
     * @see org.springframework.transaction.TransactionDefinition
     */
    public void setIsolationLevel(int isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    /**
     * 返回主键织入事务的传播行为
     *
     * @return <code>TransactionDefinition</code>定义
     * @see org.springframework.transaction.TransactionDefinition
     */
    public int getPropagationBehavior() {
        return propagationBehavior;
    }

    /**
     * 设置主键织入事务的传播行为
     *
     * @param propagationBehavior <code>TransactionDefinition</code>定义
     * @see org.springframework.transaction.TransactionDefinition
     */
    public void setPropagationBehavior(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    /**
     * 设置一个平台事务管理器类的实例。
     *
     * @param transactionManager {@link PlatformTransactionManager}
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        Assert.notNull(transactionManager, "TransactionManager object is null.");
    }
}
