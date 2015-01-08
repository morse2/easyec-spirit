package com.googlecode.easyec.spirit.ldap.transaction.support;

import com.googlecode.easyec.spirit.ldap.transaction.ContextSourceTransactionAware;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.transaction.compensating.TempEntryRenamingStrategy;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;

import static org.springframework.transaction.support.TransactionSynchronizationManager.hasResource;

/**
 * 支持<code>ContextSource</code>数据源的事务监视实现类
 *
 * @author JunJie
 */
@Aspect
public class ContextSourceTransactionAdviser implements ContextSourceTransactionAware, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ContextSourceTransactionAdviser.class);
    private static final long serialVersionUID = 3213459804560881496L;
    private final ContextSourceTransactionManagerDelegate transactionManagerDelegate = new ContextSourceTransactionManagerDelegate();
    private int order;

    public void begin() throws TransactionException {
        logger.debug("Prepare to begin LDAP transaction.");
        transactionManagerDelegate.doBegin(
            transactionManagerDelegate.doGetTransaction(), null
        );

        logger.debug("Begin LDAP transaction done.");
    }

    public void commit(DefaultTransactionStatus status) throws TransactionException {
        if (status.isNewTransaction() && hasResource(transactionManagerDelegate.getContextSource())) {
            logger.debug("Prepare to commit LDAP transaction.");

            transactionManagerDelegate.doCommit(
                new DefaultTransactionStatus(
                    transactionManagerDelegate.doGetTransaction(),
                    status.isNewTransaction(),
                    status.isNewSynchronization(),
                    status.isReadOnly(),
                    status.isDebug(),
                    status.getSuspendedResources())
            );

            logger.debug("commit LDAP transaction done.");
        }
    }

    public void rollback(DefaultTransactionStatus status) throws TransactionException {
        if (status.isNewTransaction() && hasResource(transactionManagerDelegate.getContextSource())) {
            logger.debug("Prepare to rollback LDAP transaction.");

            transactionManagerDelegate.doRollback(
                new DefaultTransactionStatus(
                    transactionManagerDelegate.doGetTransaction(),
                    status.isNewTransaction(),
                    status.isNewSynchronization(),
                    status.isReadOnly(),
                    status.isDebug(),
                    status.getSuspendedResources())
            );

            logger.debug("Rollback LDAP transaction done.");
        }
    }

    @Around("execution(* com.*..*.ldap.*Template.*(..))")
    public Object doInTransaction(ProceedingJoinPoint jp) throws Throwable {
        begin();

        logger.debug("Method [{}] prepares to invoke.", jp.getSignature().toShortString());
        Object ret = jp.proceed(jp.getArgs());
        logger.debug("Method [{}] invoked.", jp.getSignature().toShortString());

        return ret;
    }

    @After("execution(* org.springframework.transaction.PlatformTransactionManager.commit(..)) && args(status)")
    public void doCommit(JoinPoint jp, TransactionStatus status) throws Throwable {
        commit(status);
    }

    @After("execution(* org.springframework.transaction.PlatformTransactionManager.rollback(..)) && args(status)")
    public void doRollback(TransactionStatus status) throws Throwable {
        rollback(status);
    }

    @AfterThrowing(value = "execution(* org.springframework.transaction.PlatformTransactionManager.commit(..)) && args(status)" +
        " || execution(* org.springframework.transaction.PlatformTransactionManager.rollback(..)) && args(status)", throwing = "e")
    public void doThrowing(JoinPoint jp, TransactionStatus status, TransactionException e) throws Throwable {
        throwing(status, e);
    }

    public void commit(TransactionStatus status) throws TransactionException {
        commit(((DefaultTransactionStatus) status));
    }

    public void rollback(TransactionStatus status) throws TransactionException {
        rollback(((DefaultTransactionStatus) status));
    }

    public void throwing(TransactionStatus status, TransactionException e) throws TransactionException {
        logger.debug(e.getMessage(), e);

        rollback(status);
    }

    public void setRenamingStrategy(TempEntryRenamingStrategy renamingStrategy) {
        this.transactionManagerDelegate.setRenamingStrategy(renamingStrategy);
    }

    public void setContextSource(ContextSource contextSource) {
        this.transactionManagerDelegate.setContextSource(contextSource);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
