package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.mybatis.paging.AbstractMybatisPage;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * MyBatis分页对象的实现类。
 *
 * @author JunJie
 */
class MybatisPageImpl extends AbstractMybatisPage {

    private List records;

    public MybatisPageImpl(PageDialect pageDialect) {
        super(pageDialect);
    }

    public MybatisPageImpl(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    protected MybatisPageImpl(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        super(pageDialect, currentPage, pageSize, totalRecordsCount);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRecords() {
        return records != null ? records : emptyList();
    }

    public <T> void setRecords(List<T> records) {
        if (CollectionUtils.isEmpty(records)) {
            this.records = emptyList();
        } else {
            this.records = records;
        }
    }
}
