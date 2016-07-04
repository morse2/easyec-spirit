package com.googlecode.easyec.zkoss.paging.terms;

import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 过滤空的字符串的过滤器类
 *
 * @author JunJie
 */
public class BlankStringSearchTermFilter implements SearchTermsFilter {

    public boolean accept(String k, Object v) {
        if (v != null) {
            if (v instanceof String) {
                return isNotBlank((String) v);
            }
        }

        return true;
    }
}
