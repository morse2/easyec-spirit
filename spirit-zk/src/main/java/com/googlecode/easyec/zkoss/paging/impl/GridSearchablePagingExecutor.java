package com.googlecode.easyec.zkoss.paging.impl;

import com.googlecode.easyec.spirit.domain.UniqueDomainModel;
import com.googlecode.easyec.zkoss.paging.AbstractSelectablePagingExecutor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 网格组件的可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public abstract class GridSearchablePagingExecutor extends AbstractSelectablePagingExecutor<Grid, UniqueDomainModel<Serializable>, Serializable> {

    private static final long serialVersionUID = -5686383799230955310L;

    public GridSearchablePagingExecutor(Paging paging, Grid comp) {
        super(paging, comp);
    }

    public GridSearchablePagingExecutor(Paging paging, Grid comp, ConcurrentSkipListSet<Serializable> sels) {
        super(paging, comp, sels);
    }

    @Override
    protected List<Component> getHeaders() {
        Columns columns = _comp.getColumns();
        return columns != null ? columns.getChildren() : null;
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
        _comp.setModel(new ListModelList<>(records));
    }

    @Override
    protected void doSelectAfterPaging() {
        // TODO: 2019-06-21 implements here
    }

    protected void removeRowsChild(Rows rows) {
        if (null != rows) {
            rows.invalidate();

            if (isNotEmpty(rows.getChildren())) {
                rows.getChildren().clear();
            }
        }
    }
}
