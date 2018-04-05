package com.googlecode.easyec.es.paging.support;

import com.googlecode.easyec.es.paging.EsPage;
import com.googlecode.easyec.spirit.dao.dialect.impl.NoOpPageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPage;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class EsPageImpl<T> extends AbstractPage implements EsPage<T> {

    private List<?> records;

    public EsPageImpl(int currentPage, int pageSize, long totalRecordsCount) {
        super(new NoOpPageDialect(), currentPage, pageSize, (int) totalRecordsCount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getRecords() {
        return CollectionUtils.isEmpty(records)
            ? Collections.emptyList()
            : (List<T>) records;
    }

    @Override
    public <T> void setRecords(List<T> records) {
        this.records = records;
    }
}
