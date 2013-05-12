package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.zkoss.paging.listener.PagingEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

/**
 * 抽象的分页操作执行器类。
 * 此类实现了通用的功能。
 *
 * @author JunJie
 */
public abstract class AbstractPagingExecutor<T extends Component> implements PagingExecutor {

    private static final long serialVersionUID = 1013467564935458027L;
    /**
     * SLF4J日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean lazyLoad;
    /**
     * ZK分页组件对象
     */
    protected Paging _paging;
    /**
     * ZK组件对象，用于呈现分页结果
     */
    protected T _comp;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected AbstractPagingExecutor(Paging paging, T comp) {
        Assert.notNull(paging, "ZK Paging object is null.");
        Assert.notNull(comp, "ZK Component object is null.");

        this._paging = paging;
        this._comp = comp;
    }

    public void doInit() {
        if (!lazyLoad) firePaging(1); // always load first page of data

        PagingEventListener pagingEventListener = getPagingEventListener();
        Assert.notNull(pagingEventListener, "PagingEventListener object is null.");

        _paging.addEventListener("onPaging", pagingEventListener);
    }

    public boolean isLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public void firePaging(int currentPage) {
        AbstractSearchFormBean searchFormBean = new SearchFormBean();
        searchFormBean.setPageNumber(currentPage);
        firePaging(searchFormBean);
    }

    /**
     * 触发分页动作的方法。
     *
     * @param searchFormBean 表单搜索对象
     */
    protected void firePaging(AbstractSearchFormBean searchFormBean) {
        if (null == searchFormBean) searchFormBean = new SearchFormBean();

        logger.debug("Current page for paging: [" + searchFormBean.getPageNumber() + "].");

        Page page = doPaging(searchFormBean);
        Assert.notNull(page, "Page object is null after invoking method doPaging.");

        if (page.getTotalSize() < 1) clear(page);
        else if (page.getTotalSize() > 0) redraw(page);
    }

    /**
     * 得到当前页面的分页监听器类。
     *
     * @return 分页监听器的实例
     * @see PagingEventListener
     */
    protected PagingEventListener getPagingEventListener() {
        return new DefaultPagingEventListener();
    }

    /**
     * 重画分页区域内容的方法。
     *
     * @param page <code>Page</code>对象
     */
    abstract public void redraw(Page page);

    /**
     * 清空当前分页区内记录。
     * 此方法适用于没有结果集的情况。
     *
     * @param page <code>Page</code>对象
     */
    abstract public void clear(Page page);

    /**
     * 得到无结果的消息内容。
     *
     * @return 无分页结果的消息
     */
    abstract protected String getEmptyMessage();

    /**
     * 默认的分页时间监听器实现类。
     */
    private class DefaultPagingEventListener implements PagingEventListener {

        private static final long serialVersionUID = 6598392397327202954L;

        public void onEvent(PagingEvent event) throws Exception {
            AbstractPagingExecutor.this.firePaging(event.getActivePage() + 1);
        }
    }
}
