package com.googlecode.easyec.spirit.service;

import com.googlecode.easyec.spirit.dao.DataPersistenceException;
import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.dao.paging.factory.PageDelegate;
import com.googlecode.easyec.spirit.query.Query;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 基本的业务对象类。
 * <p>
 * 此类提供了初始化分页对象等方法。
 * </p>
 *
 * @author JunJie
 */
public abstract class EcService implements InitializingBean {

    /**
     * logger object of slf4j
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private PageDelegate<Page> pageDelegate;

    protected EcService() {
        // no op
    }

    @Autowired(required = false)
    public void setPageDelegate(PageDelegate<Page> pageDelegate) {
        this.pageDelegate = pageDelegate;
    }

    /**
     * 返回每页分页数。
     *
     * @return 大于0的正整数
     */
    protected Integer getPageSize() {
        return pageDelegate.getPageSize();
    }

    /**
     * 创建一个新的分页代理对象。
     *
     * @param currentPage 当前页码
     * @return {@link Page}实现
     */
    protected Page createPage(int currentPage) {
        return createPage(currentPage, getPageSize());
    }

    /**
     * 创建一个新的分页代理对象。
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示的记录数
     * @return {@link Page}实现
     */
    protected Page createPage(int currentPage, int pageSize) {
        return pageDelegate.createPage(currentPage, pageSize);
    }

    /**
     * 创建一个新的分页代理对象。
     * 此代理对象是通过一个{@link AbstractSearchFormBean}
     * 对象实例创建出来的。
     *
     * @param formBean {@link AbstractSearchFormBean}对象
     * @return {@link Page}实现
     */
    protected Page createPage(AbstractSearchFormBean formBean) {
        return pageDelegate.createPage(formBean);
    }

    /**
     * 创建一个新的分页代理对象。
     * 此代理对象是通过一个{@link AbstractSearchFormBean}
     * 对象实例和分页页数参数创建出来的。
     *
     * @param formBean {@link AbstractSearchFormBean}对象
     * @param pageSize 分页页数
     * @return {@link Page}实现
     */
    protected Page createPage(AbstractSearchFormBean formBean, int pageSize) {
        return pageDelegate.createPage(formBean, pageSize);
    }

    /**
     * 提取Query对象中设置的搜索条件
     *
     * @param query 查询条件对象
     * @return 查询条件映射
     */
    protected Map<String, Object> extractQuery(Query<?> query) {
        AbstractSearchFormBean formBean = query.getSearchFormBean();
        Map<String, Object> terms = formBean.getSearchTerms();
        // 提取当前排序信息
        terms.put("orderBy", formBean.encodeSorts());

        return terms;
    }

    /**
     * 持久化数据对象的方法。
     * 该方法包装了一般化的错误，并统一抛出
     * {@link DataPersistenceException}对象实例。
     *
     * @param cb 持久化回调对象
     * @throws DataPersistenceException
     */
    protected void persist(PersistentCallback cb) throws DataPersistenceException {
        Assert.notNull(cb, "PersistentCallback object is null.");

        try {
            int i = cb.perform();

            logger.debug("Effect rows for domain object persisted. [{}]", i);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw new DataPersistenceException(e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(pageDelegate, "Parameter 'pageDelegate' is null.");
    }
}
