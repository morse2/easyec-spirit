package com.googlecode.easyec.spirit.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.io.Serializable;

/**
 * 平台事务监视类。<br>
 * 该类配合{@link PlatformTransactionManager}事务管理对象，
 * 为其他类型的业务操作提供事务支持。并且使用此类，可以定义并支持嵌套事务
 * 的功能扩展。
 *
 * @author JunJie
 */
public interface PlatformTransactionAware extends Serializable {

    /**
     * 执行当前自定义的平台事务开始操作
     *
     * @throws TransactionException
     */
    void begin() throws TransactionException;

    /**
     * 执行当前自定义的平台事务提交操作
     *
     * @param status 事务状态
     * @throws TransactionException
     */
    void commit(TransactionStatus status) throws TransactionException;

    /**
     * 执行当前自定义的平台事务回滚操作
     *
     * @param status 事务状态
     * @throws TransactionException
     */
    void rollback(TransactionStatus status) throws TransactionException;

    /**
     * 当事务抛出<code>TransactionException</code>时，
     * 该方法即被触发，执行当前自定义的平台事务回滚操作。
     *
     * @param status 事务状态
     * @param e      事务异常对象
     * @throws TransactionException
     */
    void throwing(TransactionStatus status, TransactionException e) throws TransactionException;
}
