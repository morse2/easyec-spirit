package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.zk.ui.Component;

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

    private static final long serialVersionUID = 5973760726936949220L;
    private PagingExecutor pagingExecutor;

    /**
     * 返回分页的执行器对象
     *
     * @return <code>PagingExecutor</code>对象实例
     */
    public PagingExecutor getPagingExecutor() {
        return pagingExecutor;
    }

    @AfterCompose(superclass = true)
    public void afterBasePageVM() {
        this.pagingExecutor = createPagingExecutor();
        Assert.notNull(pagingExecutor, "PagingExecutor is also null.");

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
}
