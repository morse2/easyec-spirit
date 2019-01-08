package com.googlecode.easyec.zkoss.viewmodel;

import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import org.zkoss.zk.ui.Component;

public interface SearchablePagingViewModelAware<T extends Component> extends PagingViewModelAware<T> {

    /**
     * 返回分页的执行器对象
     *
     * @return <code>SearchablePagingExecutor</code>对象实例
     */
    SearchablePagingExecutor getPagingExecutor();

    /**
     * 返回上一个页面传递过来的查询字符串
     *
     * @return query string
     */
    String getPreQs();

    /**
     * 返回当前页面的查询字符串
     *
     * @return query string
     */
    String getCurQs();
}
