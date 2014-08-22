package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsTransform;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import com.googlecode.easyec.spirit.web.qseditors.QueryStringEditor;

import java.util.List;
import java.util.Map;

/**
 * 抽象的表单搜索Bean类。
 * 此类提供了设置搜索条件，搜索排序，搜索页码的方法。
 *
 * @author JunJie
 */
public abstract class AbstractSearchFormBean extends AbstractFormBean {

    private static final long serialVersionUID = 7364383835873539885L;

    public FormType getFormType() {
        return FormType.SEARCH_FORM;
    }

    /**
     * 返回当前表单的搜索条件
     *
     * @return 以键值对形式返回
     * @see #getRawSearchTerms()
     */
    abstract public Map<String, Object> getSearchTerms();

    /**
     * 返回当前表单的原始搜索条件。
     * 意思是调用{@link #addSearchTerm(String, Object)}
     * 方法所传递的原始值。
     *
     * @return 以键值对形式返回
     * @see #getSearchTerms()
     */
    public abstract Map<String, Object> getRawSearchTerms();

    /**
     * 以文本形式设置搜索条件
     *
     * @param params 文本形式的搜索条件集合
     */
    public abstract void setSearchTermsAsText(Map<String, String> params);

    /**
     * 返回当前表单的搜索条件。
     * 该搜索条件列表的值为文本类型。
     *
     * @return 以键值对形式返回
     */
    public abstract Map<String, String> getSearchTermsAsText();

    /**
     * 添加一个搜索条件
     *
     * @param name  字段名
     * @param value 值
     */
    abstract public void addSearchTerm(String name, Object value);

    /**
     * 添加一个搜索条件集合
     *
     * @param searchTerms 搜索条件集合对象
     */
    public abstract void addSearchTerms(Map<String, Object> searchTerms);

    /**
     * 删除一个搜索条件
     *
     * @param name 字段名
     * @return 删除成功则返回真，否则返回假
     */
    abstract public boolean removeSearchTerm(String name);

    /**
     * 判断是否有给定的搜索条件
     *
     * @param name 搜索条件的KEY
     * @return bool值
     */
    public abstract boolean hasSearchTerm(String name);

    /**
     * 返回排序字段列表
     *
     * @return 排序对象列表
     */
    abstract public List<Sort> getSorts();

    /**
     * 编码排序字段。
     * 如果没有设置排序字段，
     * 则返回空字符串。
     *
     * @return 排序信息
     */
    public abstract String encodeSorts();

    /**
     * 添加一个排序信息
     *
     * @param sort {@link Sort}
     * @return 添加成功返回真
     */
    abstract public boolean addSort(Sort sort);

    /**
     * 删除一个排序信息
     *
     * @param sort {@link Sort}
     * @return 删除成功返回真
     */
    abstract public boolean removeSort(Sort sort);

    /**
     * 返回分页页码
     *
     * @return 页码
     */
    abstract public int getPageNumber();

    /**
     * 设置搜索表单页面的页码
     *
     * @param currentPage 当前页码数
     */
    abstract public void setPageNumber(int currentPage);

    /**
     * 返回搜索条件过滤器列表
     *
     * @return 搜索条件过滤器对象列表
     */
    public abstract List<SearchTermsFilter> getFilters();

    /**
     * 返回搜索条件转换器列表
     *
     * @return 搜索条件转换器对象列表
     */
    public abstract List<SearchTermsTransform> getTransforms();

    /**
     * 返回查询字符串转换器列表
     *
     * @return 查询字符串转换器对象列表
     */
    public abstract Map<String, QueryStringEditor> getEditors();

    /**
     * 设置搜索条件过滤器列表
     *
     * @param filters 搜索条件过滤器对象列表
     */
    public abstract void setFilters(List<SearchTermsFilter> filters);

    /**
     * 设置搜索条件转换器列表
     *
     * @param transforms 搜索条件转换器对象列表
     */
    public abstract void setTransforms(List<SearchTermsTransform> transforms);

    /**
     * 设置查询字符串转换器列表
     *
     * @param editors 查询字符串转换器对象列表
     */
    public abstract void setEditors(Map<String, QueryStringEditor> editors);
}
