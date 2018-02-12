package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.zkoss.paging.AbstractSearchablePagingExecutor;
import com.googlecode.easyec.zkoss.paging.sort.SortComparator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.Selectable;

import java.util.List;

/**
 * 列表框组件的可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public abstract class ListboxSearchablePagingExecutor extends AbstractSearchablePagingExecutor<Listbox> {

    private static final long serialVersionUID = -8558100189748774107L;
    private boolean checkmark;
    private boolean multiple;

    protected ListboxSearchablePagingExecutor(Paging paging, Listbox comp) {
        super(paging, comp);

        initListbox();
    }

    /**
     * 返回当前<code>Listbox</code>
     * 的状态是否可选
     *
     * @return 布尔值
     */
    public boolean isCheckmark() {
        return checkmark;
    }

    /**
     * 返回当前<code>Listbox</code>
     * 的状态是否可多选
     *
     * @return 布尔值
     */
    public boolean isMultiple() {
        return multiple;
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

    @Override
    protected void afterPaging(Page page) {
        if (isCheckmark() != _comp.isCheckmark()) {
            _comp.setCheckmark(isCheckmark());
        }

        ListModel<Object> model = _comp.getModel();
        if (model instanceof Selectable && isMultiple() != ((Selectable) model).isMultiple()) {
            ((Selectable) model).setMultiple(isMultiple());
        }
    }

    /**
     * 初始化<code>Listbox</code>对象实例的方法
     */
    protected void initListbox() {
        this.checkmark = this._comp.isCheckmark();
        this.multiple = this._comp.isMultiple();
    }
}
