package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.zkoss.paging.PagingExecutor;
import com.googlecode.easyec.zkoss.ui.listeners.StepOutEventListener;
import com.googlecode.easyec.zkoss.ui.listeners.StepOutWithPagingEventListener;
import com.googlecode.easyec.zkoss.viewmodel.PagingViewModelAware;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.autowireBeanProperties;
import static com.googlecode.easyec.spirit.web.utils.SpringContextUtils.isInitialized;

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
@AfterCompose(superclass = true)
public abstract class BasePagingVM<T extends Component> extends BaseVM<T> implements PagingViewModelAware<T> {

    private static final long serialVersionUID = -4801582471476723836L;
    private PagingExecutor pagingExecutor;

    @Override
    public PagingExecutor getPagingExecutor() {
        return pagingExecutor;
    }

    @Override
    protected void doAfterCompose() {
        this.pagingExecutor = createPagingExecutor();
        Assert.notNull(pagingExecutor, "PagingExecutor is also null.");

        // 执行父类的方法
        super.doAfterCompose();

        if (isInitialized()) {
            // 使用Spring来为PageExecutor实例注入依赖对象
            autowireBeanProperties(pagingExecutor, false);
        }

        // 执行分页执行类的初始化前置方法
        beforePagingExecutorInit(pagingExecutor);
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
    abstract protected PagingExecutor createPagingExecutor();

    /**
     * 在<code>PagingExecutor</code>对象被初始化之前执行的方法
     *
     * @param exec PagingExecutor对象实例
     * @see PagingExecutor#doInit()
     */
    protected void beforePagingExecutorInit(PagingExecutor exec) {
        logger.trace("beforePagingExecutorInit() done..");
    }

    @Override
    protected StepOutEventListener createStepOutEventListener() {
        StepOutWithPagingEventListener lsnr
            = new StepOutWithPagingEventListener(
            getSelf(), getSelf().getParent()
        );

        lsnr.setPagingExecutor(pagingExecutor);

        return lsnr;
    }
}
