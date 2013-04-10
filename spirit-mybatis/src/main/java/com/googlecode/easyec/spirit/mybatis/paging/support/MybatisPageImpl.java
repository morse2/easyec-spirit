package com.googlecode.easyec.spirit.mybatis.paging.support;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.mybatis.paging.AbstractMybatisPage;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * MyBatis分页对象的实现类。
 *
 * @author JunJie
 */
class MybatisPageImpl extends AbstractMybatisPage {

    private List<?> records;

    public MybatisPageImpl(PageDialect pageDialect) {
        super(pageDialect);
    }

    public MybatisPageImpl(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    protected MybatisPageImpl(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        super(pageDialect, currentPage, pageSize, totalRecordsCount);
    }

    public List<?> getRecords() {
        return records;
    }

    public void setRecords(List<?> records) {
        if (CollectionUtils.isEmpty(records)) {
            this.records = Collections.emptyList();
        } else {
            this.records = records;
        }
    }
}
