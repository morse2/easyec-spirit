package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.UniqueDomainModel;
import com.googlecode.easyec.zkoss.paging.AbstractSelectablePagingExecutor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.Selectable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.IterableUtils.matchesAny;
import static org.zkoss.zk.ui.event.Events.ON_SELECT;

/**
 * 列表框组件的可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public abstract class ListboxSearchablePagingExecutor extends AbstractSelectablePagingExecutor<Listbox, UniqueDomainModel<Serializable>, Serializable> {

    private static final long serialVersionUID = 1406215074477539278L;
    private boolean checkmark;
    private boolean multiple;

    public ListboxSearchablePagingExecutor(Paging paging, Listbox comp) {
        this(paging, comp, null);
    }

    public ListboxSearchablePagingExecutor(Paging paging, Listbox comp, ConcurrentSkipListSet<Serializable> sels) {
        super(paging, comp, sels);
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

        // call super method.
        super.afterPaging(page);
    }

    @Override
    protected void doSelectAfterPaging() {
        Set<Serializable> _selections = getSelections();
        if (isCheckmark() && isMultiple() && isNotEmpty(_selections)) {
            List<Listitem> items = _comp.getItems();
            if (isNotEmpty(items)) {
                List<UniqueDomainModel<?>> selectedObjects = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    UniqueDomainModel<Serializable> obj = _comp.<UniqueDomainModel<Serializable>>getModel().getElementAt(i);
                    if (obj == null) continue;
                    if (matchesAny(_selections, obj::evaluate)) {
                        selectedObjects.add(obj);
                    }
                }

                if (isNotEmpty(selectedObjects)) {
                    ((ListModelList<UniqueDomainModel>)
                        _comp.<UniqueDomainModel>getListModel())
                        .setSelection(selectedObjects);
                }
            }
        }
    }

    @Override
    protected List<Component> getHeaders() {
        Listhead listhead = _comp.getListhead();
        return listhead != null
            ? listhead.getChildren()
            : Collections.emptyList();
    }

    /**
     * 初始化<code>Listbox</code>对象实例的方法
     */
    protected void initListbox() {
        this.checkmark = _comp.isCheckmark();
        this.multiple = _comp.isMultiple();

        if (isCheckmark() || isMultiple()) {
            this._comp.addEventListener(ON_SELECT, createSelectEventListener());
        }
    }
}
