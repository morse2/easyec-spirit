package com.googlecode.easyec.spirit.dao.id.support;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 采用同步锁机制的主键序列生成决定的拦截类
 * <p/>
 * 此类拦截DAO层的insertXXX和createXXX方法，
 * 用于为PO对象生成主键的行为。
 * <p/>
 *
 * @author JunJie
 */
@Aspect
public class SynchronousSequenceGenerateDecisionInterceptor extends AbstractSequenceGenerateDecisionInterceptor {

    // ReentrantLock object instance
    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    @Before(before_expr)
    public void before(JoinPoint joinPoint, Object o) throws Exception {
        try {
            reentrantLock.lock();
            logger.debug("Do identifier generation.");

            sequenceGenerator.process(o);
            logger.debug("Finish identifier generation.");
        } finally {
            reentrantLock.unlock();
        }
    }
}
