package com.googlecode.easyec.zkoss.paging.impl;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @deprecated It will be removed in the future.
 */
@Deprecated
public abstract class ListboxSelectablePagingExecutor extends ListboxSearchablePagingExecutor {

    public ListboxSelectablePagingExecutor(Paging paging, Listbox comp) {
        super(paging, comp);
    }

    public ListboxSelectablePagingExecutor(Paging paging, Listbox comp, ConcurrentSkipListSet<Serializable> sels) {
        super(paging, comp, sels);
    }
}
