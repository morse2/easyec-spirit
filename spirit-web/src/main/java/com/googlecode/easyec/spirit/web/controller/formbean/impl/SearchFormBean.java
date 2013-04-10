package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 表单搜索的Bean类。
 *
 * @author JunJie
 */
public class SearchFormBean extends AbstractSearchFormBean {

    private static final long serialVersionUID = 3216967564312120125L;
    private Map<String, Object> searchTerms = new LinkedHashMap<String, Object>();
    private List<Sort> sorts = new LinkedList<Sort>();
    private int currentPage = 1;

    public Map<String, Object> getSearchTerms() {
        return searchTerms;
    }

    public void addSearchTerm(String name, Object value) {
        searchTerms.put(name, value);
    }

    public boolean removeSearchTerm(String name) {
        return searchTerms.remove(name) != null;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public boolean addSort(Sort sort) {
        return sort != null && sorts.add(sort);
    }

    public boolean removeSort(Sort sort) {
        return sorts.remove(sort);
    }

    @Override
    public int getPageNumber() {
        return currentPage;
    }

    @Override
    public void setPageNumber(int currentPage) {
        this.currentPage = currentPage > 0 ? currentPage : 1;
    }
}
