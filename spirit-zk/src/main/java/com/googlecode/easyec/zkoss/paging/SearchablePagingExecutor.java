package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import org.zkoss.zk.ui.Component;

import java.util.Map;

/**
 * 可搜索的分页操作执行器类。
 *
 * @author JunJie
 */
public interface SearchablePagingExecutor extends PagingExecutor {

    /**
     * 设置搜索组件的范围是否包含在ZK的Page范围内
     *
     * @param searchSelectorsInPage 布尔值
     */
    void setSearchSelectorsInPage(boolean searchSelectorsInPage);

    /**
     * 触发分页执行的方法。
     *
     * @param currentPage     当前页码
     * @param withSearchTerms 标识是否带入搜索条件
     */
    void firePaging(int currentPage, boolean withSearchTerms);

    /**
     * 设置查询条件组件的搜索范围。
     * 从给定的组件下开始搜索。
     *
     * @param searchScope 搜索范围的组件对象
     */
    void setSearchScope(Component searchScope);

    /**
     * 设置URL的查询参数
     *
     * @param qs 查询字符串
     */
    void setQueryString(String qs);

    /**
     * 将搜索条件编码成URL查询字符串
     *
     * @return URL查询字符串
     */
    String encodeSearchTerms();

    /**
     * 返回并得到一个包装了搜索条件
     * 的搜索Bean对象
     *
     * @return 搜索Bean对象
     * @see AbstractSearchFormBean
     */
    AbstractSearchFormBean getSearchFormBean();

    /**
     * 返回原始设置的搜索条件集合
     *
     * @return 原始设置的搜索条件
     */
    Map<String, Object> getRawSearchTerms();

    /**
     * 返回当前的搜索条件信息。
     *
     * @return 搜索条件列表
     */
    Map<String, Object> getSearchTerms();

    /**
     * 返回可变的原始的搜索条件集合
     *
     * @return 可变的原始搜索条件集合
     */
    Map<String, Object> getMutableRawSearchTerms();

    /**
     * 返回当前的可变搜索条件信息。
     *
     * @return 可变搜索条件列表
     */
    Map<String, Object> getMutableSearchTerms();

    /**
     * 返回当前的不可变搜索条件信息。
     *
     * @return 不可变搜索条件信息
     */
    Map<String, Object> getImmutableSearchTerms();

    /**
     * 移除存在的不可变的搜索条件
     *
     * @param key 搜索条件KEY
     * @return bool值
     */
    boolean removeImmutableSearchTerm(String key);

    /**
     * 添加一个不可变的搜索条件
     *
     * @param key   搜索条件KEY
     * @param value 搜索条件VAL
     * @return 添加成功返回真，否则返回假
     */
    boolean addImmutableSearchTerm(String key, Object value);
}
