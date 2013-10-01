package com.googlecode.easyec.spirit.dao.identifier.impl;

import com.googlecode.easyec.spirit.dao.identifier.IdentifierWeavingInterceptor;
import com.googlecode.easyec.spirit.dao.identifier.factory.IdentifierHolderFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.concurrent.locks.ReentrantLock;

import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * 默认的主键标识织入拦截器类。
 * 此类拦截DAO层的insertXXX和createXXX方法，
 * 用于为PO对象生成主键的行为。
 * <p>
 * 当Spring平台的事务可用，则使用平台的事务管理机制来控制主键生成机制的同步操作。
 * 否则，则使用Java的可重入锁机制来控制主键生成机制的同步操作。
 * </p>
 *
 * @author JunJie
 */
@Aspect
public class DefaultIdentifierWeavingInterceptor implements IdentifierWeavingInterceptor, Ordered, InitializingBean {

    private static final Logger logger      = LoggerFactory.getLogger(DefaultIdentifierWeavingInterceptor.class);
    private static final String before_expr =
        "execution(* com.*..*.dao.*Dao.create*(..)) && args(o)" +
            "||execution(* com.*..*.dao.*Dao.insert*(..)) and args(o)";

    private IdentifierHolderFactory    identifierHolderFactory;
    private PlatformTransactionManager transactionManager;
    private int                        order;

    // transactional isolation level
    private int isolationLevel      = ISOLATION_READ_COMMITTED;
    // transactional propagation behavior
    private int propagationBehavior = PROPAGATION_REQUIRES_NEW;

    // ReentrantLock object instance
    private ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 返回主键织入事务的隔离级别
     *
     * @return <code>TransactionDefinition</code>定义
     * @see TransactionDefinition
     */
    public int getIsolationLevel() {
        return isolationLevel;
    }

    /**
     * 设置当前主键织入事务的隔离级别
     *
     * @param isolationLevel <code>TransactionDefinition</code>定义
     * @see TransactionDefinition
     */
    public void setIsolationLevel(int isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    /**
     * 返回主键织入事务的传播行为
     *
     * @return <code>TransactionDefinition</code>定义
     * @see TransactionDefinition
     */
    public int getPropagationBehavior() {
        return propagationBehavior;
    }

    /**
     * 设置主键织入事务的传播行为
     *
     * @param propagationBehavior <code>TransactionDefinition</code>定义
     * @see TransactionDefinition
     */
    public void setPropagationBehavior(int propagationBehavior) {
        this.propagationBehavior = propagationBehavior;
    }

    /**
     * 设置一个平台事务管理器类的实例。
     *
     * @param transactionManager {@link PlatformTransactionManager}
     */
    @Autowired(required = false)
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 设置AOP执行的顺序值。
     *
     * @param order 排序值。数值越大，优先权越低
     */
    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    /**
     * 设置主键标识句柄工厂类。
     *
     * @param identifierHolderFactory {@link IdentifierHolderFactory}
     */
    public void setIdentifierHolderFactory(IdentifierHolderFactory identifierHolderFactory) {
        this.identifierHolderFactory = identifierHolderFactory;
    }

    @Before(before_expr)
    public void weaveBefore(JoinPoint joinPoint, final Object o) throws Throwable {
        if (null != transactionManager) {
            TransactionTemplate t = new TransactionTemplate(transactionManager);
            t.setPropagationBehavior(propagationBehavior);
            t.setIsolationLevel(isolationLevel);

            t.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        logger.debug("Do identifier generation.");
                        identifierHolderFactory.generate(o);
                        logger.debug("Finish identifier generation.");
                    } catch (Exception e) {
                        status.setRollbackOnly();
                    }
                }
            });
        } else {
            try {
                reentrantLock.lock();

                logger.debug("Do identifier generation.");
                identifierHolderFactory.generate(o);
                logger.debug("Finish identifier generation.");
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(identifierHolderFactory, "IdentifierHolderFactory object cannot be null.");
    }
}
