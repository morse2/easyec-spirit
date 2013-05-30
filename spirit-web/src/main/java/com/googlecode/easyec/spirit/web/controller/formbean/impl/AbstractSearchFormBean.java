package com.googlecode.easyec.spirit.web.controller.formbean.impl;

import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽象的表单搜索Bean类。
 * 此类提供了设置搜索条件，搜索排序，搜索页码的方法。
 *
 * @author JunJie
 */
public abstract class AbstractSearchFormBean extends AbstractFormBean {

    private static final long serialVersionUID = -6225171420085008346L;

    public FormType getFormType() {
        return FormType.SEARCH_FORM;
    }

    /**
     * 返回当前表单的搜索条件
     *
     * @return 以键值对形式返回
     */
    abstract public Map<String, Object> getSearchTerms();

    /**
     * 添加一个搜索条件
     *
     * @param name  字段名
     * @param value 值
     */
    abstract public void addSearchTerm(String name, Object value);

    /**
     * 删除一个搜索条件
     *
     * @param name 字段名
     * @return 删除成功则返回真，否则返回假
     */
    abstract public boolean removeSearchTerm(String name);

    /**
     * 返回排序字段列表
     *
     * @return 排序对象列表
     */
    abstract public Set<Sort> getSorts();

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
}
