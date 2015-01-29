package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.web.utils.WebUtils;
import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.zkoss.zk.ui.Executions.getCurrent;

/**
 * 支持可搜索的分页框架的视图模型的基类。
 * <p>
 * 该类默认提供了搜索和重置搜索条件的方法
 * </p>
 *
 * @author JunJie
 * @see SearchablePagingExecutor
 */
public abstract class BaseSearchablePagingVM<T extends Component> extends BasePagingVM<T> {

    private static final long serialVersionUID = 6496229113691506381L;
    private String preQs;

    /**
     * 返回上一个页面传递过来的查询字符串
     *
     * @return query string
     */
    public String getPreQs() {
        if (isBlank(preQs)) return "";
        return new StringBuffer()
            .append("?")
            .append(preQs)
            .toString();
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

        String qs = getCurQs();

        if (isBlank(qs)) return original;

        return new StringBuffer()
            .append(original)
            .append(qs)
            .toString();
    }

    /**
     * 返回当前页面的查询字符串
     *
     * @return query string
     */
    public String getCurQs() {
        String qs = getPagingExecutor().encodeSearchTerms();
        logger.debug("Current page's query string: [{}].", qs);

        return isNotBlank(qs)
            ? new StringBuffer()
            .append("?")
            .append(qs)
            .toString()
            : "";
    }

    @Override
    public SearchablePagingExecutor getPagingExecutor() {
        return (SearchablePagingExecutor) super.getPagingExecutor();
    }

    @Init(superclass = true)
    public void initBaseSearchablePagingVM() {
        preQs = ((HttpServletRequest) getCurrent().getNativeRequest()).getQueryString();
    }

    /**
     * 执行搜索命令的方法
     */
    @Command("search")
    public void search() {
        getPagingExecutor().firePaging(1);
    }

    /**
     * 执行重置命令的方法
     */
    @Command("reset")
    public void reset() {
        getPagingExecutor().firePaging(1, false);
    }

    /**
     * 初始化一个支持搜索的分页执行器对象实例。
     * <p>
     * PS：该方法将在@AfterCompose注解方法执行后被调用。
     * </p>
     *
     * @return <code>SearchablePagingExecutor</code>对象
     */
    @Override
    abstract public SearchablePagingExecutor createPagingExecutor();

    @Override
    protected final void beforePagingExecutorInit(PagingExecutor exec) {
        super.beforePagingExecutorInit(exec);

        beforeSearchablePagingExecutorInit((SearchablePagingExecutor) exec);
        setImmutableSearchTerms(WebUtils.decodeQueryString(preQs));
    }

    /**
     * 在<code>SearchablePagingExecutor</code>对象被初始化之前执行的方法
     *
     * @param exec SearchablePagingExecutor对象实例
     * @see PagingExecutor#doInit()
     */
    protected void beforeSearchablePagingExecutorInit(SearchablePagingExecutor exec) {
        exec.setQueryString(preQs);
    }

    /**
     * 设置当前分页执行器对象的不可变的查询条件的方法
     *
     * @param queryStringMap 查询条件集合
     */
    protected void setImmutableSearchTerms(Map<String, String> queryStringMap) {
        logger.trace("Method 'setImmutableSearchTerms(Map)' done.");
    }
}
