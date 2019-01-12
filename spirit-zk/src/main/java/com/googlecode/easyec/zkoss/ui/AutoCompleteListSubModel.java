package com.googlecode.easyec.zkoss.ui;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListSubModel;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 自动完成的<code>ListSubModel</code>
 * 的基类。该类提供获取用户输入的值子集的基础实现方法。
 *
 * @param <E> 泛型对象
 * @author junjie
 */
public abstract class AutoCompleteListSubModel<E> extends ListModelList<E> implements ListSubModel<E> {

    private static final ListModelList EMPTY = new ListModelList<>();
    private static final long serialVersionUID = 468468027674855259L;

    public AutoCompleteListSubModel() {
        this(false);
    }

    public AutoCompleteListSubModel(boolean multiple) {
        super(new ArrayList<>(), true);
        setMultiple(multiple);
    }

    @SuppressWarnings("unchecked")
    public ListModel<E> getSubModel(Object value, int nRows) {
        if (!hasValue(value)) return EMPTY;

        List<E> result = fetchItems(value);
        if (CollectionUtils.isEmpty(result)) {
            return EMPTY;
        }

        if (result instanceof ListModel) {
            return ((ListModel<E>) result);
        }

        return new ListModelList<>(result);
    }

    @Override
    public E getElementAt(int j) {
        if (j < 0) return null;
        List<E> _list = getInnerList();
        if (CollectionUtils.isEmpty(_list)) return null;
        return super.getElementAt(j);
    }

    /**
     * 该方法判断用户输入的内容是否有效。
     * 此方法默认实现了一般情况，比如空字符串内容，
     * 是否是null等。如遇复杂的情况，则由子类进行判断。
     *
     * @param value 用户输入的值
     * @return 输入的值是否有效
     */
    protected boolean hasValue(Object value) {
        return value != null && isNotBlank(value.toString());
    }

    /**
     * 依据用户输入的值，匹配并返回符合
     * 条件的数据的子集合。
     *
     * @param inputValue 用户输入的值
     * @return 匹配用户输入的数据集合
     */
    protected abstract List<E> fetchItems(Object inputValue);
}




