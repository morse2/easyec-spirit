package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.googlecode.easyec.spirit.web.controller.formbean.annotations.SearchTermType;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;
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

    private static final long serialVersionUID = 3758430453970592169L;

    /* 搜索条件过滤器类 */
    private List<SearchTermsFilter> filters = new ArrayList<SearchTermsFilter>();

    /* 搜索条件转换类 */
    private List<SearchTermsTransform> transforms = new ArrayList<SearchTermsTransform>();

    /* 搜索条件集合 */
    private Map<String, Object> searchTerms = new LinkedHashMap<String, Object>();

    /* 排序信息集合 */
    private Set<Sort> sorts = new HashSet<Sort>();

    /* 当前分页的页码 */
    private int currentPage = 1;

    public SearchFormBean() { }

    public SearchFormBean(List<SearchTermsTransform> transforms) {
        this(transforms, Collections.<SearchTermsFilter>emptyList());
    }

    public SearchFormBean(List<SearchTermsTransform> transforms, List<SearchTermsFilter> filters) {
        if (null != transforms && !transforms.isEmpty()) {
            this.transforms.addAll(transforms);
        }

        if (null != filters && !filters.isEmpty()) {
            this.filters.addAll(filters);
        }
    }

    public Map<String, Object> getSearchTerms() {
        return new HashMap<String, Object>(searchTerms);
    }

    public void addSearchTerm(String name, Object value) {
        if (null == name || null == value) {
            logger.debug("Term's name or value either is null.");

            return;
        }

        boolean acceptTerm = true;
        for (SearchTermsFilter filter : filters) {
            if (!filter.accept(name, value)) {
                acceptTerm = false;

                break;
            }
        }

        if (acceptTerm) {
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
