package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.domain.UniqueDomainModel;
import com.googlecode.easyec.zkoss.paging.listener.PagingEventListener;
import com.googlecode.easyec.zkoss.paging.listener.SelectEventListener;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.IterableUtils.matchesAny;

public abstract class AbstractSelectablePagingExecutor<T extends Component, M extends UniqueDomainModel<E>, E extends Serializable>
    extends AbstractSearchablePagingExecutor<T> implements PagingSelectable<M, E> {

    private static final long serialVersionUID = 8768458236135119959L;
    /* 默认支持跨页选择功能 */
    private boolean spread = true;
    private ConcurrentSkipListSet<E> _selections = new ConcurrentSkipListSet<>();

    public AbstractSelectablePagingExecutor(Paging paging, T comp) {
        super(paging, comp);
    }

    public AbstractSelectablePagingExecutor(Paging paging, T comp, ConcurrentSkipListSet<E> sels) {
        super(paging, comp);

        if (isNotEmpty(sels)) {
            this._selections.addAll(sels);
        }
    }

    @Override
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
    protected void afterPaging(Page page) {
        super.afterPaging(page);

        if (isNotEmpty(_selections)) doSelectAfterPaging();
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
    protected SelectEventListener<T, M> createSelectEventListener() {
        return new DefaultListboxSelectEventListener();
    }

    /**
     * 在执行完分页操作后，执行选择的操作的方法
     */
    abstract protected void doSelectAfterPaging();

    /**
     * Listbox选择事件默认的内部实现类
     *
     * @author junjie
     */
    private class DefaultListboxSelectEventListener implements SelectEventListener<T, M> {

        private static final long serialVersionUID = -584182747792948506L;

        @Override
        public void onEvent(SelectEvent<T, M> event) throws Exception {
            Set<M> selectedObjects = event.getSelectedObjects();
            if (isNotEmpty(selectedObjects)) {
                selectedObjects.stream()
                    .filter(obj -> !matchesAny(_selections, obj::evaluate))
                    .forEach(obj -> _selections.add(obj.get()));
            }
        }
    }

    private class DefaultSelectPagingEventListener implements PagingEventListener {

        private static final long serialVersionUID = -1870268107094579432L;

        @Override
        public void onEvent(PagingEvent event) throws Exception {
            AbstractSelectablePagingExecutor<T, M, E> _exec = AbstractSelectablePagingExecutor.this;
            if (!_exec.isSpread()) _exec.clear();

            _exec.firePaging(event.getActivePage() + 1);
        }
    }
}
