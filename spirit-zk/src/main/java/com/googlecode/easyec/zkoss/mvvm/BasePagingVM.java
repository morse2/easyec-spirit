package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.autowireBeanProperties;

/**
 * 支持分页框架的视图模型的基类。
 * <p>
 * 子类可以继承并实现MVVM模型下的Table的分页功能。
 * </p>
 *
 * @author JunJie
 * @see PagingExecutor
 */
@Init(superclass = true)
public abstract class BasePagingVM<T extends Component> extends BaseVM<T> {

    private static final long serialVersionUID = -2466151502994352867L;
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
    }
}
