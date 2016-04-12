package com.googlecode.easyec.spirit.dao.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractJdbcPage;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * JDBC分页的实现类
 *
 * @author JunJie
 */
class JdbcPageImpl extends AbstractJdbcPage {

    private List records;

    public JdbcPageImpl(PageDialect pageDialect) {
        super(pageDialect);
    }

    public JdbcPageImpl(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    protected JdbcPageImpl(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
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
