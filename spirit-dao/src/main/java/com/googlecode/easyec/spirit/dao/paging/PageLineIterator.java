/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.easyec.spirit.dao.paging;

import java.util.Iterator;

/**
 * 分页当前记录行迭代器类。
 * 此类用于迭代当前页记录的行号。
 *
 * @author junjie
 */
public interface PageLineIterator<T extends Number> extends Iterator<T> {

    /**
     * 得到当前页的第一条记录的编号.
     * <p>
     * 例如: 每页显示的记录数为10条,
     * 那么第一页的第一条记录编号为1;
     * 第二页的第一条记录编号则为11.
     * </p>
     *
     * @return a <code>java.lang.Number</code> object
     */
    T getFirstLineNumber();

    /**
     * 得到当前页的最后一条记录的编号.
     * <p>
     * 例如: 每页显示的记录数为10条,
     * 那么第一页的最后一条记录编号为10;
     * 第二页的最后一条记录编号则为20.
     * </p>
     *
     * @return a <code>java.lang.Number</code> object
     */
    T getLastLineNumber();

    /**
     * 得到当前页的下一条记录的编号.
     * 当取完当前页的最后一条记录编号后,
     * 继续调用该方法,则返回-1.
     *
     * @return a <code>java.lang.Number</code> object
     */
    T getNextLineNumber();
}
