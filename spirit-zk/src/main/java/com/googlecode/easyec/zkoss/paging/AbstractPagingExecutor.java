package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.spirit.web.controller.sorts.DefaultSort;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import com.googlecode.easyec.zkoss.paging.listener.PagingEventListener;
import com.googlecode.easyec.zkoss.paging.listener.SortFieldEventListener;
import com.googlecode.easyec.zkoss.paging.sort.SortComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SortEvent;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.googlecode.easyec.spirit.web.controller.sorts.Sort.SortTypes.DESC;

/**
 * 抽象的分页操作执行器类。
 * 此类实现了通用的功能。
 *
 * @author JunJie
 */
public abstract class AbstractPagingExecutor<T extends Component> implements PagingExecutor {

    private static final long serialVersionUID = 5612179200246645081L;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean lazyLoad;
    private boolean visible = true;
    /**
     * ZK分页组件对象
     */
    protected Paging    _paging;
    /**
     * ZK组件对象，用于呈现分页结果
     */
    protected T         _comp;
    protected Set<Sort> sortList;

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
        sortList = new HashSet<Sort>();

        // 添加分页监听事件实例
        PagingEventListener pagingEventListener = getPagingEventListener();
        Assert.notNull(pagingEventListener, "PagingEventListener object is null.");

        _paging.addEventListener("onPaging", pagingEventListener);

        // 如果分页不是延迟加载的，则默认加载第一页数据
        if (!lazyLoad) firePaging(1); // 总是加载第一页的数据
        else _comp.setVisible(visible); // 如果延迟加载，则设置显示区域可见或不可见
    }

    public boolean isLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public void setVisibleIfLazyLoad(boolean visible) {
        this.visible = visible;
    }

    public void firePaging(int currentPage) {
        AbstractSearchFormBean searchFormBean = new SearchFormBean();
        searchFormBean.setPageNumber(currentPage);
        firePaging(searchFormBean);
    }

    public Paging getPaging() {
        return this._paging;
    }

    /**
     * 触发分页动作的方法。
     *
     * @param searchFormBean 表单搜索对象
     */
    protected void firePaging(AbstractSearchFormBean searchFormBean) {
        if (null == searchFormBean) searchFormBean = new SearchFormBean();

        logger.debug("Current page for paging: [" + searchFormBean.getPageNumber() + "].");

        // 如果有排序字段，则进行添加排序条件
        if (!sortList.isEmpty()) {
            for (Sort sort : sortList) {
                boolean b = searchFormBean.addSort(sort);
                if (b) {
                    String name = sort.getName().replaceAll("\\.", "_");
                    String value = name + "_" + sort.getType();
                    logger.debug("Sort parameter: [{}], value: [{}]", name, value);

                    searchFormBean.addSearchTerm(name, value);
                }
            }
        }

        beforePaging(searchFormBean);
        Page page = doPaging(searchFormBean);
        Assert.notNull(page, "Page object is null after invoking method doPaging.");
        afterPaging(page);

        // 如果页面为延迟加载，此时则应该显示组件
        if (lazyLoad) _comp.setVisible(true);
        if (page.getTotalSize() < 1) doClear(page);
        else if (page.getTotalSize() > 0) doRedraw(page);
    }

    /**
     * 分页方法执行前的前置方法
     *
     * @param searchFormBean 搜索条件对象
     */
    protected void beforePaging(AbstractSearchFormBean searchFormBean) {
        logger.debug("Nothing to do.");
    }

    /**
     * 分页方法执行后的后置方法
     *
     * @param page 分页结果对象
     */
    protected void afterPaging(Page page) {
        logger.debug("Nothing to do.");
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
     * 返回当前分页使用的字段排序监听类对象
     *
     * @return 字段排序监听实例
     */
    protected SortFieldEventListener getSortFieldEventListener() {
        return new DefaultSortFieldEventListener();
    }

    /**
     * 返回一个新的字段排序的比较类对象。
     *
     * @param index     列索引
     * @param ascending 标识是否是升序
     * @return <code>Comparator</code>
     */
    protected SortComparator createSortComparator(int index, boolean ascending) {
        return null; // 默认不做实现，交由子类根据实际情况进行字段排序
    }

    /**
     * 执行清除ZK控件内容的操作
     *
     * @param page 分页结果对象
     */
    protected void doClear(Page page) {
        _paging.setPageSize(page.getPageSize());
        _paging.setTotalSize(page.getTotalRecordsCount());

        clear(page);
    }

    /**
     * 执行重画ZK控件内容的操作
     *
     * @param page 分页结果对象
     */
    private void doRedraw(Page page) {
        _paging.setPageSize(page.getPageSize());
        _paging.setActivePage(page.getCurrentPage() - 1);
        _paging.setTotalSize(page.getTotalRecordsCount());

        redraw(page);
    }

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

    /**
     * 默认的基于数据库字段进行排序的默认的事件监听类
     */
    private class DefaultSortFieldEventListener implements SortFieldEventListener {

        private static final long serialVersionUID = 1271754732132111589L;

        public void onEvent(SortEvent event) throws Exception {
            Listheader listheader = (Listheader) event.getTarget();
            String direction = listheader.getSortDirection();
            if ("natural".equals(direction)) {
                Comparator ascending = listheader.getSortAscending();
                Assert.notNull(ascending, "There was not set ascending sort comparator.");

                SortComparator sc = (SortComparator) ascending;
                sortList.clear();
                sortList.add(new DefaultSort(sc.getFullSortField()));
            } else if ("ascending".equals(direction)) {
                Comparator descending = listheader.getSortDescending();
                Assert.notNull(descending, "There was not set descending sort comparator.");

                SortComparator sc = (SortComparator) descending;
                sortList.clear();
                sortList.add(new DefaultSort(sc.getFullSortField(), DESC));
            } else if ("descending".equals(direction)) {
                Comparator ascending = listheader.getSortAscending();
                Assert.notNull(ascending, "There was not set ascending sort comparator.");

                SortComparator sc = (SortComparator) ascending;
                sortList.clear();
                sortList.add(new DefaultSort(sc.getFullSortField()));
            } else {
                logger.debug("Undefined sort direction.");

                event.stopPropagation();
                return;
            }

            firePaging(_paging.getActivePage() + 1);
        }
    }
}
