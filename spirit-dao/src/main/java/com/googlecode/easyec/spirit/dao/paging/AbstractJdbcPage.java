package com.googlecode.easyec.spirit.dao.paging;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的JDBC分页对象类
 *
 * @author JunJie
 */
public abstract class AbstractJdbcPage extends AbstractPage implements JdbcPage, JdbcPageWritable {

    private List<Sort> sorts = new ArrayList<Sort>();
    private Map<String, Object> params = new HashMap<String, Object>();

    protected AbstractJdbcPage(PageDialect pageDialect) {
        super(pageDialect);
    }

    protected AbstractJdbcPage(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    protected AbstractJdbcPage(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        super(pageDialect, currentPage, pageSize, totalRecordsCount);
    }

    @Override
    public void setSearchTerms(Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            this.params.putAll(params);
        }
    }

    @Override
    public Map<String, Object> getSearchTerms() {
        return this.params;
    }

    @Override
    public List<Sort> getSorts() {
        return sorts;
    }

    @Override
    public void setSorts(List<Sort> sorts) {
        if (CollectionUtils.isNotEmpty(sorts)) {
            this.sorts.addAll(sorts);
        }
    }
}
