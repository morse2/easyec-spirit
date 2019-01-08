package com.googlecode.easyec.zkoss.viewmodel;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import org.zkoss.zk.ui.Component;

public interface PagingViewModelAware<T extends Component> extends ViewModelAware<T> {

    /**
     * 返回分页的执行器对象
     *
     * @return <code>PagingExecutor</code>对象实例
     */
    PagingExecutor getPagingExecutor();
}
