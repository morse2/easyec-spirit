package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.domain.UniqueDomainModel;

import java.io.Serializable;
import java.util.Set;

/**
 * 支持列表页面勾选的接口类
 *
 * @author junjie
 */
public interface PagingSelectable<M extends UniqueDomainModel<E>, E extends Serializable> {

    /**
     * 返回当前选中的数据对象集合
     *
     * @return <code>Set</code>
     */
    Set<E> getSelections();

    /**
     * 清除当前已选记录
     */
    void clear();

    /**
     * 设置是否支持跨页选择
     *
     * @param spread 布尔值
     */
    void setSpread(boolean spread);

    /**
     * 返回当前选择是否支持跨页
     *
     * @return 布尔值
     */
    boolean isSpread();
}
