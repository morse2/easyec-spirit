package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.autowireBeanProperties;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.zkoss.zk.ui.Executions.getCurrent;

/**
 * 支持分页框架的视图模型的基类。
 * <p>
 * 子类可以继承并实现MVVM模型下的Table的分页功能。
 * </p>
 *
 * @author JunJie
 * @see PagingExecutor
 */
public abstract class BasePagingVM<T extends Component> extends BaseVM<T> {

    private PagingExecutor pagingExecutor;

    private String preQs;

    /**
     * 返回分页的执行器对象
     *
     * @return <code>PagingExecutor</code>对象实例
     */
    public PagingExecutor getPagingExecutor() {
        return pagingExecutor;
    }

    @Init(superclass = true)
    public void initBasePagingVM() {
        preQs = ((HttpServletRequest) getCurrent().getNativeRequest()).getQueryString();
    }

    @AfterCompose(superclass = true)
    public void afterBasePageVM() {
        this.pagingExecutor = createPagingExecutor();
        Assert.notNull(pagingExecutor, "PagingExecutor is also null.");

        // 执行分页执行类的初始化前置方法
        beforePagingExecutorInit(pagingExecutor);
        // 使用Spring来为PageExecutor实例注入依赖对象
        autowireBeanProperties(pagingExecutor, false);
        // 初始化分页执行类的初始方法
        this.pagingExecutor.doInit();
    }

    /**
     * 初始化一个分页执行器对象实例。
     * <p>
     * PS：该方法将在@AfterCompose注解方法执行后被调用。
     * </p>
     *
     * @return <code>PagingExecutor</code>对象
     */
    abstract public PagingExecutor createPagingExecutor();

    /**
     * 在<code>PagingExecutor</code>对象被初始化之前执行的方法
     *
     * @param exec PagingExecutor对象实例
     * @see PagingExecutor#doInit()
     */
    protected void beforePagingExecutorInit(PagingExecutor exec) {
        logger.trace("beforePagingExecutorInit() done..");

        if (exec instanceof SearchablePagingExecutor) {
            ((SearchablePagingExecutor) exec).setQueryString(preQs);
        }
    }

    /**
     * 为给定的URL追加上一个页面的查询字符串
     *
     * @param original 原始url
     * @return 可能追加了查询字符串的url
     */
    protected String appendPreQs(String original) {
        if (isBlank(original)) return "";
        if (isBlank(preQs)) return original;
        return new StringBuffer()
                .append(original)
                .append("?")
                .append(preQs)
                .toString();
    }

    /**
     * 为给定的URL追加当前页面的查询字符串
     *
     * @param original 原始url
     * @return 可能追加了查询字符串的url
     */
    protected String appendCurQs(String original) {
        if (isBlank(original)) return "";

        String qs = null;
        if (pagingExecutor instanceof SearchablePagingExecutor) {
            qs = ((SearchablePagingExecutor) pagingExecutor).encodeSearchTerms();
            logger.debug("Current page's query string: [{}].", qs);
        }

        if (isBlank(qs)) return original;

        return new StringBuffer()
                .append(original)
                .append("?")
                .append(qs)
                .toString();
    }
}
