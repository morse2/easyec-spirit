package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import org.zkoss.zul.Paging;

import java.io.Serializable;

/**
 * 分页操作的执行器类。
 * 此接口定义了分页通用的方法。
 *
 * @author JunJie
 */
public interface PagingExecutor extends Serializable {

    /**
     * 初始化方法。在组件被初始化时候执行一次。
     */
    void doInit();

    /**
     * 执行并调用实际的分页算法。
     *
     * @param bean 搜索Bean实例
     * @return 分页结果集对象
     * @see com.googlecode.easyec.spirit.dao.paging.Page
     */
    Page doPaging(AbstractSearchFormBean bean);

    /**
     * 返回当前分页是否是延迟加载数据的。
     * 默认为假，表示即时加载页面数据。
     *
     * @return 真表示第一次进入页面不会加载数据
     */
    boolean isLazyLoad();

    /**
     * 设置第一次进入页面是否要延迟加载数据。
     *
     * @param lazyLoad 布尔值
     */
    void setLazyLoad(boolean lazyLoad);

    /**
     * 当设置了延迟加载时候，
     * 此方法设置主组件的可见性。
     *
     * @param visible 真为可见，否则不可见
     */
    void setVisibleIfLazyLoad(boolean visible);

    /**
     * 触发分页动作的方法。
     *
     * @param currentPage 当前页码
     */
    void firePaging(int currentPage);

    /**
     * 返回当前分页执行器对象依赖的实际分页对象
     *
     * @return <code>Paging</code>
     */
    Paging getPaging();
}
