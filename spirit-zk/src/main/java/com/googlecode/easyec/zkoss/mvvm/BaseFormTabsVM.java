package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.zkoss.ui.form.FormWrapper;
import com.googlecode.easyec.zkoss.viewmodel.FormViewModelAware;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static com.googlecode.easyec.zkoss.utils.ExecUtils.getNativeRequest;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Init(superclass = true)
@AfterCompose(superclass = true)
public abstract class BaseFormTabsVM<T extends Component, M extends GenericPersistentDomainModel<E>, E extends Serializable>
    extends BaseTabsVM<T> implements FormViewModelAware<T, M, E> {

    private static final long serialVersionUID = -6786164010893090311L;
    private FormWrapper<M, E> _wrp;
    private String preQs;

    @Override
    public M getDomainModel() {
        return _wrp != null ? _wrp.getDomainModel() : null;
    }

    @Override
    public boolean isNew() {
        return _wrp.isNew();
    }

    @Override
    public String getPreQs() {
        if (isBlank(preQs)) return "";
        return new StringBuffer()
            .append("?")
            .append(preQs)
            .toString();
    }

    /**
     * 创建一个持久化域模型对象实例。
     * 此方法只有当获取不到参数
     * {@code ARG_FORM_OBJECT}的时候，
     * 才会被使用。
     *
     * @return 域模型对象实例
     */
    abstract protected M createIfNull();

    /**
     * 初始化加载域模型对象实例。
     *
     * @param model 当前参数传递的域模型对象
     * @return 加载完后的域模型对象实例
     */
    protected M loadModel(M model) {
        return loadModel(model.getUidPk());
    }

    /**
     * 初始化加载域模型对象实例。
     * 该方法默认由 loadModel(M)
     * 方法调用，如果loadModel(M)
     * 被覆盖，此方法可能不会被触发，
     * 或由用户按需触发。
     *
     * @param id 模型的ID
     * @return 加载完后的域模型对象实例
     * @see #loadModel(GenericPersistentDomainModel)
     */
    protected M loadModel(E id) {
        return null;
    }

    @Override
    protected void doInit() {
        super.doInit();

        HttpServletRequest request = getNativeRequest();
        if (request != null) preQs = request.getQueryString();

        this._wrp
            = initialize(
            this::createIfNull,
            this::loadModel
        );

        getArgs().put(ARG_FORM_OBJECT, getDomainModel());
        getArgs().put(ARG_CHECK_UIDPK, false);
        getArgs().put(WITH_FORM_OBJ, true);
    }
}
