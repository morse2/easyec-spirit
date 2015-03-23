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

    private static final long serialVersionUID = 6361109293049839555L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected ListboxSearchablePagingExecutor(Paging paging, Listbox comp) {
        super(paging, comp);
    }

    private boolean checkmark;
    private boolean multiple;

    /**
     * 返回此列表框中的数据是否可选。
     * <p>
     * 如果此方法返回假，那么调用方法
     * {@link #setMultiple(boolean)}
     * 不会产生任何效果。
     * </p>
     *
     * @return 返回真代表可勾选
     */
    public boolean isCheckmark() {
        return checkmark;
    }

    /**
     * 设置此列表框中的数据是否可选。
     * <p>
     * 如果此方法设置为假，那么调用方法
     * {@link #setMultiple(boolean)}
     * 不会产生任何效果。
     * </p>
     *
     * @param checkmark 复选标识
     */
    public void setCheckmark(boolean checkmark) {
        this.checkmark = checkmark;
    }

    /**
     * 返回此列表框中的数据是否是多选。
     *
     * @return 返回真表示为复选框
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * 设置此列表框中的数据是否是多选。
     *
     * @param multiple 复选框标识
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
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
        _comp.setEmptyMessage(getEmptyMessage());
    }

    @Override
    protected void doRedraw(List<?> records) {
        ListModelList<Object> model = new ListModelList<Object>(records);
        model.setMultiple(isMultiple());

        _comp.getItems().clear();
        _comp.setModel(model);
        _comp.setCheckmark(isCheckmark());
    }
}
