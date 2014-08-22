package com.googlecode.easyec.spirit.query;

import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.spirit.web.controller.sorts.DefaultSort;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.List;

/**
 * 数据层查询条件的抽象的封装类
 *
 * @author JunJie
 */
public abstract class AbstractQuery<T> implements Query<T> {

    private SearchFormBean formBean = new SearchFormBean();

    public AbstractSearchFormBean getSearchFormBean() {
        return formBean;
    }

    public T searchFormBean(AbstractSearchFormBean bean) {
        // 复制参数bean中的一些附加属性
        formBean.setEditors(bean.getEditors());
        formBean.setFilters(bean.getFilters());
        formBean.setTransforms(bean.getTransforms());

        // 复制参数bean的已设置的搜索条件和当前页码
        this.formBean.addSearchTerms(bean.getRawSearchTerms());
        this.formBean.setPageNumber(bean.getPageNumber());

        // 复制参数bean的排序信息
        List<Sort> sorts = formBean.getSorts();
        for (Sort sort : sorts) {
            this.formBean.addSort(sort);
        }

        return getSelf();
    }

    /**
     * 设置分页的页码
     *
     * @param pageNumber 页码，必须大于0
     */
    protected void setPageNumber(int pageNumber) {
        formBean.setPageNumber(pageNumber);
    }

    /**
     * 添加一个搜索条件
     *
     * @param key 搜索条件的KEY
     * @param val 搜索条件的VALUE
     */
    protected void addSearchTerm(String key, Object val) {
        formBean.addSearchTerm(key, val);
    }

    /**
     * 添加一个排序条件
     *
     * @param column    排序的列
     * @param direction 排序的方向
     */
    protected void addSort(String column, Sort.SortTypes direction) {
        formBean.addSort(new DefaultSort(column, direction));
    }

    /**
     * 返回自身的对象实例
     *
     * @return 自身实例
     */
    abstract protected T getSelf();
}
