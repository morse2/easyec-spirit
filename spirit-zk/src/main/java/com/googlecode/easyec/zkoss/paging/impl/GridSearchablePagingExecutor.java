package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.zkoss.paging.AbstractSearchablePagingExecutor;
import com.googlecode.easyec.zkoss.paging.sort.SortComparator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 网格组件的可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public abstract class GridSearchablePagingExecutor extends AbstractSearchablePagingExecutor<Grid> {

    private static final long serialVersionUID = -4038324977135177347L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected GridSearchablePagingExecutor(Paging paging, Grid comp) {
        super(paging, comp);
    }

    @Override
    public void doInit() {
        super.doInit();

        List<Component> children = _comp.getColumns().getChildren();
        for (int i = 0; i < children.size(); i++) {
            Component child = children.get(i);

            if (null == child) continue;
            if (child instanceof Listheader) {
                SortComparator ascending = createSortComparator(i, true);
                SortComparator descending = createSortComparator(i, false);

                if (null != ascending && null != descending) {
                    ((Listheader) child).setSortAscending(ascending);
                    ((Listheader) child).setSortDescending(descending);

                    // 默认为Listheader添加监听类实例
                    child.addEventListener(Events.ON_SORT, getSortFieldEventListener());
                }
            }
        }
    }

    @Override
    protected void doClear() {
        removeRowsChild(_comp.getRows());
        _comp.setModel(new ListModelList<>());
        _comp.setEmptyMessage(getEmptyMessage());
    }

    @Override
    protected void doRedraw(List<?> records) {
        removeRowsChild(_comp.getRows());
        _comp.setModel(new ListModelList<Object>(records));
    }

    private void removeRowsChild(Rows rows) {
        if (null != rows) {
            rows.invalidate();

            if (isNotEmpty(rows.getChildren())) {
                rows.getChildren().clear();
            }
        }
    }
}
