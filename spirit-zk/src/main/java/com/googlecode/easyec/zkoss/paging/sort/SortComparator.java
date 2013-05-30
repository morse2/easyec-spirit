package com.googlecode.easyec.zkoss.paging.sort;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Comparator;

/**
 * 对象字段排序比较类
 *
 * @author JunJie
 */
public abstract class SortComparator<T> implements Comparator<T> {

    private String  sortField;
    private String  fieldAlias;
    private boolean ascending;

    protected SortComparator(String sortField, boolean ascending) {
        this(sortField, ascending, null);
    }

    protected SortComparator(String sortField, boolean ascending, String fieldAlias) {
        Assert.notNull(sortField, "Sort field cannot be null.");
        this.sortField = sortField;
        this.fieldAlias = fieldAlias;
        this.ascending = ascending;
    }

    /**
     * 返回数据库表的映射别名。
     *
     * @return 别名，不带点号
     */
    public String getFieldAlias() {
        return this.fieldAlias;
    }

    /**
     * 返回数据库需要排序的字段名称。
     *
     * @return 数据库表的字段名称
     */
    public String getSortField() {
        return this.sortField;
    }

    /**
     * 返回是否是升序排序
     *
     * @return 真表示升序，假表示降序
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * 返回完整的排序字段表示。
     * <p>
     * 例如：别名为t，字段column1，
     * 则调用此方法返回的值为t.column1
     * </p>
     *
     * @return 完整的字段排序表示的值
     */
    public String getFullSortField() {
        StringBuffer sb = new StringBuffer();

        if (StringUtils.isNotBlank(fieldAlias)) {
            sb.append(fieldAlias).append(".");
        }

        return sb.append(sortField).toString();
    }

    public static class Builder<T> {

        private String  sortField;
        private String  fieldAlias;
        private boolean ascending;

        public Builder sortField(String sortField) {
            Assert.notNull(sortField, "Sort field cannot be null.");
            this.sortField = sortField;
            return this;
        }

        public Builder ascending(boolean ascending) {
            this.ascending = ascending;
            return this;
        }

        public Builder fieldAlias(String fieldAlias) {
            this.fieldAlias = fieldAlias;
            return this;
        }

        public SortComparator<T> build() {
            return new DefaultGlobalSortComparator<T>(sortField, ascending, fieldAlias);
        }
    }
}
