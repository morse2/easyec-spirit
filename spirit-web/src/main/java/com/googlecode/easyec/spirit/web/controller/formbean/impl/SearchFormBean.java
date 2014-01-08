package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.googlecode.easyec.spirit.web.controller.formbean.annotations.SearchTermType;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsTransform;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * 表单搜索的Bean类。
 *
 * @author JunJie
 */
public class SearchFormBean extends AbstractSearchFormBean {

    private static final long serialVersionUID = 7238698734745183686L;
    private List<SearchTermsTransform> transforms = new ArrayList<SearchTermsTransform>();
    private Map<String, Object> searchTerms = new LinkedHashMap<String, Object>();
    private Set<Sort>           sorts       = new HashSet<Sort>();
    private int                 currentPage = 1;

    public SearchFormBean() { }

    public SearchFormBean(List<SearchTermsTransform> transforms) {
        if (null != transforms && !transforms.isEmpty()) {
            this.transforms.addAll(transforms);
        }
    }

    public Map<String, Object> getSearchTerms() {
        return searchTerms;
    }

    public void addSearchTerm(String name, Object value) {
        if (null == name || null == value) {
            logger.debug("Term's name or value either is null.");

            return;
        }

        Object thisVal = value;
        for (SearchTermsTransform transform : transforms) {
            Class<? extends SearchTermsTransform> cls = transform.getClass();
            if (cls.isAnnotationPresent(SearchTermType.class)) {
                Class<?>[] classes = cls.getAnnotation(SearchTermType.class).value();

                if (ArrayUtils.isNotEmpty(classes)) {
                    for (Class type : classes) {
                        if (type.isInstance(thisVal)) {
                            thisVal = transform.transform(name, thisVal);
                        }
                    }
                }
            }
        }

        searchTerms.put(name, thisVal);
    }

    public boolean removeSearchTerm(String name) {
        return searchTerms.remove(name) != null;
    }

    public Set<Sort> getSorts() {
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
