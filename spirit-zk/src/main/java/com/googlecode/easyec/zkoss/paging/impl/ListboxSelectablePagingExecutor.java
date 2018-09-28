package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.UniqueDomainModel;
import com.googlecode.easyec.zkoss.paging.PagingSelectable;
import com.googlecode.easyec.zkoss.paging.listener.PagingEventListener;
import com.googlecode.easyec.zkoss.paging.listener.SelectEventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.IterableUtils.matchesAny;
import static org.zkoss.zk.ui.event.Events.ON_SELECT;

public abstract class ListboxSelectablePagingExecutor<T extends UniqueDomainModel<E>, E extends Serializable>
    extends ListboxSearchablePagingExecutor implements PagingSelectable {

    private static final long serialVersionUID = 770142279296592632L;
    private boolean spread = true;

    private ConcurrentSkipListSet<E> _selections = new ConcurrentSkipListSet<>();

    protected ListboxSelectablePagingExecutor(Paging paging, Listbox comp) {
        this(paging, comp, null);
    }

    protected ListboxSelectablePagingExecutor(Paging paging, Listbox comp, Set<E> sels) {
        super(paging, comp);

        if (isNotEmpty(sels)) {
            this._selections.addAll(sels);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<E> getSelections() {
        return !_selections.isEmpty()
            ? unmodifiableSet(_selections)
            : emptySet();
    }

    @Override
    public void clear() {
        if (!_selections.isEmpty()) _selections.clear();
    }

    @Override
    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    @Override
    public boolean isSpread() {
        return spread;
    }

    @Override
    protected void initListbox() {
        super.initListbox();

        if (isCheckmark() || isMultiple()) {
            this._comp.addEventListener(ON_SELECT, createSelectEventListener());
        }
    }

    @Override
    protected void afterPaging(Page page) {
        super.afterPaging(page);

        if (isCheckmark() && isMultiple() && isNotEmpty(_selections)) {
            List<Listitem> items = this._comp.getItems();
            if (isNotEmpty(items)) {
                List<T> selectedObjects = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    T obj = this._comp.<T>getModel().getElementAt(i);
                    if (obj == null) continue;
                    if (matchesAny(_selections, obj::evaluate)) {
                        selectedObjects.add(obj);
                    }
                }

                if (isNotEmpty(selectedObjects)) {
                    ((ListModelList<T>) this._comp.<T>getListModel())
                        .setSelection(selectedObjects);
                }
            }
        }
    }

    @Override
    protected PagingEventListener getPagingEventListener() {
        return new DefaultSelectPagingEventListener();
    }

    /**
     * 构造一个选择事件的监听类
     *
     * @return <code>SelectEventListener</code>
     */
    protected SelectEventListener<Listbox, T> createSelectEventListener() {
        return new DefaultListboxSelectEventListener();
    }

    /**
     * Listbox选择事件默认的内部实现类
     *
     * @author junjie
     */
    private class DefaultListboxSelectEventListener implements SelectEventListener<Listbox, T> {

        private static final long serialVersionUID = 6622069139463614748L;

        @Override
        public void onEvent(SelectEvent<Listbox, T> event) throws Exception {
            Set<T> selectedObjects = event.getSelectedObjects();
            if (isNotEmpty(selectedObjects)) {
                for (T obj : selectedObjects) {
                    if (!matchesAny(_selections, obj::evaluate)) {
                        _selections.add(obj.get());
                    }
                }
            }

            Set<T> unselectedObjects = event.getUnselectedObjects();
            if (isNotEmpty(unselectedObjects)) {
                for (T obj : unselectedObjects) {
                    _selections.removeIf(obj::evaluate);
                }
            }
        }
    }

    private class DefaultSelectPagingEventListener implements PagingEventListener {

        private static final long serialVersionUID = -1870268107094579432L;

        @Override
        public void onEvent(PagingEvent event) throws Exception {
            ListboxSelectablePagingExecutor<T, E> _exec = ListboxSelectablePagingExecutor.this;
            if (!_exec.isSpread()) _exec.clear();

            _exec.firePaging(event.getActivePage() + 1);
        }
    }
}
