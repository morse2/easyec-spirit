package com.googlecode.easyec.zkoss.paging.sort;

/**
 * 默认全局字段排序器类。
 * <p>
 * 此类默认不做比较逻辑。
 * </p>
 *
 * @author JunJie
 */
final class DefaultGlobalSortComparator<T> extends SortComparator<T> {

    DefaultGlobalSortComparator(String sortField, boolean ascending, String fieldAlias) {
        super(sortField, ascending, fieldAlias);
    }

    public int compare(T o1, T o2) {
        return 0;
    }
}
