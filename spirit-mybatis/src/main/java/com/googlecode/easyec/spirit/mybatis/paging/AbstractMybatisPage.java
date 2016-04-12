package com.googlecode.easyec.spirit.mybatis.paging;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.AbstractPage;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * 抽象的MyBatis分页对象类。
 *
 * @author JunJie
 */
public abstract class AbstractMybatisPage extends AbstractPage implements MybatisPage, MybatisPageWritable {

    private List<Sort> sorts = new ArrayList<Sort>();
    private Object parameterObject;

    protected AbstractMybatisPage(PageDialect pageDialect) {
        super(pageDialect);
    }

    protected AbstractMybatisPage(PageDialect pageDialect, int currentPage, int pageSize) {
        super(pageDialect, currentPage, pageSize);
    }

    protected AbstractMybatisPage(PageDialect pageDialect, int currentPage, int pageSize, int totalRecordsCount) {
        super(pageDialect, currentPage, pageSize, totalRecordsCount);
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object parameterObject) {
        this.parameterObject = parameterObject;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        if (isNotEmpty(sorts)) this.sorts.addAll(sorts);
    }
}
