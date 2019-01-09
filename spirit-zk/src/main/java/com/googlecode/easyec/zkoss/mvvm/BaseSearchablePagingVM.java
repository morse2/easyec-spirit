package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.web.utils.WebUtils;
import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.paging.PagingSelectable;
import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import com.googlecode.easyec.zkoss.utils.ExecUtils;
import com.googlecode.easyec.zkoss.viewmodel.SearchablePagingViewModelAware;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 支持可搜索的分页框架的视图模型的基类。
 * <p>
 * 该类默认提供了搜索和重置搜索条件的方法
 * </p>
 *
 * @author JunJie
 * @see SearchablePagingExecutor
 */
@Init(superclass = true)
@AfterCompose(superclass = true)
public abstract class BaseSearchablePagingVM<T extends Component> extends BasePagingVM<T> implements SearchablePagingViewModelAware<T> {

    private static final long serialVersionUID = -2512442216157483045L;
    private String preQs;

    @Override
    public String getPreQs() {
        if (isBlank(preQs)) return "";
        return new StringBuffer()
            .append("?")
            .append(preQs)
            .toString();
    }

    @Override
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
    protected void doInit() {
        super.doInit();

        HttpServletRequest request = ExecUtils.getNativeRequest();
        if (request != null) preQs = request.getQueryString();
    }

    @Override
    public SearchablePagingExecutor getPagingExecutor() {
        return (SearchablePagingExecutor) super.getPagingExecutor();
    }

    /**
     * 执行搜索命令的方法
     */
    @Command
    public void search() {
        SearchablePagingExecutor exec = getPagingExecutor();
        if (exec instanceof PagingSelectable) {
            ((PagingSelectable) exec).clear();
        }

        exec.firePaging(1);
    }

    /**
     * 执行重置命令的方法
     */
    @Command
    public void reset() {
        SearchablePagingExecutor exec = getPagingExecutor();
        if (exec instanceof PagingSelectable) {
            ((PagingSelectable) exec).clear();
        }

        exec.firePaging(1, false);
    }

    /**
     * 刷新当前页面数据
     */
    @Command
    public void refresh() {
        getPagingExecutor().firePaging(
            getPagingExecutor().getPaging().getActivePage() + 1
        );
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
    abstract protected SearchablePagingExecutor createPagingExecutor();

    @Override
    protected final void beforePagingExecutorInit(PagingExecutor exec) {
        super.beforePagingExecutorInit(exec);

        beforeSearchablePagingExecutorInit((SearchablePagingExecutor) exec);
        setImmutableSearchTerms(
            (SearchablePagingExecutor) exec,
            WebUtils.decodeQueryString(preQs)
        );
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
     * @param exec           SearchablePagingExecutor对象实例
     * @param queryStringMap 查询条件集合
     */
    protected void setImmutableSearchTerms(SearchablePagingExecutor exec, Map<String, String> queryStringMap) {
        logger.trace("Method 'setImmutableSearchTerms(Map)' done.");
    }
}
