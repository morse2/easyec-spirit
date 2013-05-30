package com.googlecode.easyec.spirit.mybatis.paging.config;

import com.googlecode.easyec.spirit.dao.dialect.PageDialect;
import com.googlecode.easyec.spirit.dao.paging.PageProxy;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPage;
import com.googlecode.easyec.spirit.mybatis.paging.MybatisPageWritable;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.sorts.Sort;

import java.util.ArrayList;

/**
 * 默认内部的Mybatis分页委托类。
 *
 * @author JunJie
 */
final class DefaultMybatisPageDelegate extends PageDelegate<MybatisPage> {

    /**
     * 构造方法。
     *
     * @param pageSize    分页页数
     * @param pageDialect 分页方言对象
     * @param pageProxy   分页代理对象
     */
    public DefaultMybatisPageDelegate(Integer pageSize, PageDialect pageDialect, PageProxy<MybatisPage> pageProxy) {
        super(pageSize, pageDialect, pageProxy);
    }

    @Override
    public MybatisPage createPage(AbstractSearchFormBean bean) {
        return createPage(bean, getPageSize());
    }

    @Override
    public MybatisPage createPage(AbstractSearchFormBean bean, int pageSize) {
        MybatisPage page = createPage(bean.getPageNumber(), pageSize);

        if (page instanceof MybatisPageWritable) {
            // 设置搜索条件
            ((MybatisPageWritable) page).setParameterObject(bean.getSearchTerms());
            // 设置排序条件
            ((MybatisPageWritable) page).setSorts(new ArrayList<Sort>(bean.getSorts()));
        }

        return page;
    }
}
