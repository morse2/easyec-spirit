package com.googlecode.easyec.spirit.web.webservice.ui.paging.impl;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPage;
import com.googlecode.easyec.spirit.web.webservice.ui.paging.WebPage;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * 默认的分页对象类。该类仅供WS作为数据转换时使用
 *
 * @author JunJie
 */
final class DefaultWebPage extends AbstractPage implements WebPage {

    private List<?> records;
    private Map<String, String[]> parameters;

    DefaultWebPage(PageDialect pageDialect) {
        super(pageDialect);
    }

    DefaultWebPage(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    DefaultWebPage(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        super(pageDialect, currentPage, pageSize, totalRecordsCount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getRecords() {
        return isEmpty(records) ? null : (List<T>) records;
    }

    @Override
    public <T> void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public Map<String, String[]> getParameters() {
        return MapUtils.isEmpty(parameters) ? Collections.emptyMap() : parameters;
    }

    @Override
    public void setParameters(Map<String, String[]> params) {
        this.parameters = params;
    }
}
