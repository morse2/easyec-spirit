package com.googlecode.easyec.spirit.mybatis.executor.support;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.select;
import static org.apache.commons.collections4.IterableUtils.matchesAny;
import static org.apache.commons.collections4.functors.InstanceofPredicate.instanceOfPredicate;

/**
 * <code>SqlSession</code>执行异常类。
 * 该类包含了MyBatis的SqlSession执行时发生的异常信息。
 *
 * @author JunJie
 */
public class SqlSessionNestedException extends RuntimeException {

    private static final long serialVersionUID = 2388255546427969442L;
    private List<Exception> linkedExceptions
        = new LinkedList<Exception>();

    /**
     * 返回当前异常列表
     *
     * @return 异常列表
     */
    public List<Exception> getLinkedExceptions() {
        return unmodifiableList(linkedExceptions);
    }

    /**
     * 返回给定类型的关联的异常类
     *
     * @param type 参与判定的异常类型
     * @return 符合条件的异常信息列表
     */
    @SuppressWarnings("unchecked")
    public List<Exception> getLinkedException(Class<? extends Exception> type) {
        return new ArrayList<Exception>(
            select(linkedExceptions, instanceOfPredicate(type))
        );
    }

    /**
     * 返回给定类型的关联的第一个异常类
     *
     * @param type 参与判定的异常类型
     * @return 符合条件的异常信息
     */
    public Exception getFirstLinkedException(Class<? extends Exception> type) {
        List<Exception> list = getLinkedException(type);
        return isEmpty(list) ? null : list.get(0);
    }

    /**
     * 添加一个新的异常对象到当前列表中
     *
     * @param e 异常对象
     */
    public void addException(Exception e) {
        if (null != e) linkedExceptions.add(e);
    }

    /**
     * 返回当前列表中是否有异常信息
     *
     * @return 有新的异常，返回真；否则返回假
     */
    public boolean hasExceptions() {
        return !linkedExceptions.isEmpty();
    }

    /**
     * 判断给定的异常类型是否存在
     *
     * @param exceptionType 异常类型
     * @return 存在则返回真，否则返回假
     */
    public boolean hasException(Class<? extends Exception> exceptionType) {
        return hasExceptions() && matchesAny(
            linkedExceptions,
            instanceOfPredicate(exceptionType)
        );
    }

    @Override
    public void printStackTrace() {
        for (Exception ex : linkedExceptions) {
            ex.printStackTrace();
        }
    }
}
