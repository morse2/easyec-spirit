package com.googlecode.easyec.spirit.query;

import com.googlecode.easyec.spirit.dao.paging.Page;

import java.util.List;

/**
 * 查询对象的抽象适配器类。
 * <p>
 * 该类适配了Query接口中的方法，
 * 子类可以通过继承此类来选择实现
 * 接口的方法
 * </p>
 *
 * @author JunJie
 */
public abstract class AbstractQueryAdapter<T> extends AbstractQuery<T> implements Query<T> {

    public Page listPage(int currentPage) {
        return null;
    }

    public Page listPage(int currentPage, int pageSize) {
        return null;
    }

    public long count() {
        return 0;
    }

    public <U> List<U> list() {
        return null;
    }
}
