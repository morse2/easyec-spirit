package com.googlecode.easyec.zkoss.ui.oper.impl;

import com.googlecode.easyec.zkoss.paging.AbstractSelectablePagingExecutor;
import com.googlecode.easyec.zkoss.paging.PagingSelectable;
import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.event.Event;

import java.util.Collections;
import java.util.Set;

/**
 * 支持<code>SearchablePagingExecutor</code>
 * 对象操作的基础实现类
 *
 * @author junjie
 */
public abstract class PagingSelectableOperation extends SubmitButtonOperation {

    private static final long serialVersionUID = -534339846964590976L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private SearchablePagingExecutor pagingExecutor;

    public PagingSelectableOperation(SearchablePagingExecutor pagingExecutor) {
        Assert.notNull(pagingExecutor, "SearchablePagingExecutor cannot be null.");
        this.pagingExecutor = pagingExecutor;
    }

    public SearchablePagingExecutor getPagingExecutor() {
        return pagingExecutor;
    }

    /**
     * 获取当前列表页上选中的数据集合。
     * 如果当前<code>PagingExecutor</code>不是
     * <code>PagingSelectable</code>的实例对象，
     * 那么该方法直接返回空的集合。
     *
     * @param <E> 数据集合中的对象类型
     * @return <code>Set</code>集合对象
     * @see AbstractSelectablePagingExecutor
     */
    @SuppressWarnings("unchecked")
    protected <E> Set<E> getSelections() {
        SearchablePagingExecutor exec = getPagingExecutor();
        return exec instanceof PagingSelectable
            ? ((PagingSelectable) exec).getSelections()
            : Collections.emptySet();
    }

    /**
     * 清除当前列表中已选中的数据集合。
     * 如果<code>PagingExecutor</code>不是
     * <code>PagingSelectable</code>的实例对象，
     * 那么该方法的操作将被忽略
     */
    protected void clearSelections() {
        SearchablePagingExecutor exec = getPagingExecutor();
        if (exec instanceof PagingSelectable) {
            ((PagingSelectable) exec).clear();
        }
    }

    @Override
    protected void onSuccess(Event event) {
        // 清除数据
        clearSelections();
        // 刷新页面
        getPagingExecutor().firePaging(
            getPagingExecutor().getPaging().getActivePage() + 1
        );
    }
}
