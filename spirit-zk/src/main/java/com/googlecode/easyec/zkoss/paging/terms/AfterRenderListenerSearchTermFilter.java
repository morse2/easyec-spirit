package com.googlecode.easyec.zkoss.paging.terms;

import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;
import com.googlecode.easyec.zkoss.paging.listener.ComboboxAfterRenderEventListener;

import static com.googlecode.easyec.zkoss.paging.AbstractSearchablePagingExecutor.AFTER_RENDER_LISTENER;

/**
 * 过滤名为afterRenderListener
 * 的属性的过滤类。
 *
 * @author JunJie
 */
public class AfterRenderListenerSearchTermFilter implements SearchTermsFilter {

    public boolean accept(String k, Object v) {
        return !AFTER_RENDER_LISTENER.equals(k)
            && !(v instanceof ComboboxAfterRenderEventListener);
    }
}
