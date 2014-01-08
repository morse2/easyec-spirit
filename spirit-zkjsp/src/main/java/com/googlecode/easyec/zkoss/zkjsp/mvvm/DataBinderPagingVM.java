package com.googlecode.easyec.zkoss.zkjsp.mvvm;

import com.googlecode.easyec.spirit.dao.paging.Page;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.zkoss.mvvm.BasePagingVM;
import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.paging.SearchablePagingExecutor;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinderInit;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Paging;

import java.util.Map;
import java.util.Set;

/**
 * 支持数据绑定分页的视图模型基类
 *
 * @author JunJie
 * @see AnnotateDataBinderInit
 */
@Init(superclass = true)
public abstract class DataBinderPagingVM<T extends Component> extends BasePagingVM<T> {

    private static final long serialVersionUID = -9159356518862401645L;

    private boolean lazyLoad;
    private ListModelList<Object> listModel = new ListModelList<Object>();

    /**
     * 返回ZK Grid、Listbox的数据对象模型
     *
     * @return <code>ListModelList</code>对象
     */
    public ListModelList<Object> getListModel() {
        return listModel;
    }

    /**
     * 返回标识当前分页执行对象是否为延迟加载模式
     *
     * @return 延迟加载的话，为真；否则为假
     */
    public boolean isLazyLoad() {
        return lazyLoad;
    }

    @AfterCompose(superclass = true)
    public void afterDataBinderPagingInit() {
        if (!lazyLoad) {
            // 执行加载初始数据的动作
            PagingExecutor exec = getPagingExecutor();

            SearchFormBean formBean = new SearchFormBean();
            // 获取搜索条件，如果搜索机制可用
            if (exec instanceof SearchablePagingExecutor) {
                Map<String, Object> terms = ((SearchablePagingExecutor) exec).getSearchTerms();
                Set<String> keySet = terms.keySet();
                for (String key : keySet) {
                    formBean.addSearchTerm(key, terms.get(key));
                }
            }

            Page page = exec.doPaging(formBean);

            // 获取注入的ZK分页对象
            Paging paging = exec.getPaging();
            paging.setPageSize(page.getPageSize());
            paging.setActivePage(page.getCurrentPage() - 1);
            paging.setTotalSize(page.getTotalRecordsCount());

            listModel.addAll(page.getRecords());
        }
    }

    @Override
    protected void beforePagingExecutorInit(PagingExecutor exec) {
        if (!(lazyLoad = exec.isLazyLoad())) exec.setLazyLoad(true);
    }
}
