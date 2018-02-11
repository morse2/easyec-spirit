package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.zkoss.paging.AbstractSearchablePagingExecutor;
import com.googlecode.easyec.zkoss.paging.sort.SortComparator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;

import java.util.List;

/**
 * 列表框组件的可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public abstract class ListboxSearchablePagingExecutor extends AbstractSearchablePagingExecutor<Listbox> {

    private static final long serialVersionUID = -5354868178904741831L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected ListboxSearchablePagingExecutor(Paging paging, Listbox comp) {
        super(paging, comp);
    }

    @Override
    public void doInit() {
        super.doInit();

        List<Component> children = _comp.getListhead().getChildren();
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
        _comp.getItems().clear();
        _comp.setModel(new ListModelList<>());
        _comp.setEmptyMessage(getEmptyMessage());
    }

    @Override
    protected void doRedraw(List<?> records) {
        _comp.getItems().clear();
        _comp.setModel(new ListModelList<>(records));
    }
}
