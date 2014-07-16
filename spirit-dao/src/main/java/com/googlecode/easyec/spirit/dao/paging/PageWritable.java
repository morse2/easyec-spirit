package com.googlecode.easyec.spirit.dao.paging;

import java.util.List;

/**
 * 分页可写标识接口类。
 * 该类提供了对分页对象可写的方法。
 * 调用此类中的方法，可能会改变分页结果和行为。
 *
 * @author JunJie
 */
public interface PageWritable {

    /**
     * 设置记录结果。
     *
     * @param records 当前分页的结果集
     */
    <T> void setRecords(List<T> records);

    /**
     * 设置分页总的记录数。
     *
     * @param totalRecordsCount 总记录数
     */
    void setTotalRecordsCount(int totalRecordsCount);

    /**
     * 设置下一页可用的标识。
     *
     * @param nextPageAvailable 下一页是否可用
     */
    void setNextPageAvailable(boolean nextPageAvailable);

    /**
     * 设置上一页可用的标识。
     *
     * @param prevPageAvailable 上一页是否可用
     */
    void setPrevPageAvailable(boolean prevPageAvailable);
}
