package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.domain.PersistentDomainModel;
import com.googlecode.easyec.zkoss.ui.form.FormWrapper;
import com.googlecode.easyec.zkoss.viewmodel.FormViewModelAware;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static com.googlecode.easyec.zkoss.utils.ExecUtils.getNativeRequest;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 基于表单的模型视图-视图模型基本类。
 * 此基类提供了当前操作是新增还是编辑的标记。
 * <p>
 * 实现方式为：分别从
 * <ol>
 * <li>{@code Executions.getCurrent().getArg()}</li>
 * <li>{@code org.zkoss.zk.ui.Session.getAttribute()}</li>
 * </ol>
 * 三个范围中来获取变量
 * {@link FormViewModelAware#ARG_FORM_OBJECT}，如果变量不为null，并且对象实例来自于
 * {@link PersistentDomainModel}，并且 {@link PersistentDomainModel#getUidPk()}
 * 不为空，则判断为此操作为编辑；否则判断为新增。
 * 注意：如果对象信息是从Session范围中获取，则获取变量之后即将该变量从Session范围中删除。
 * </p>
 *
 * @author JunJie
 */
@Init(superclass = true)
@AfterCompose(superclass = true)
public abstract class BaseFormVM<T extends Component, M extends GenericPersistentDomainModel<E>, E extends Serializable>
    extends BaseVM<T> implements FormViewModelAware<T, M, E> {

    private static final long serialVersionUID = -4023845175817918439L;
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
        HttpServletRequest request = getNativeRequest();
        if (request != null) preQs = request.getQueryString();

        this._wrp
            = initialize(
            this::createIfNull,
            this::loadModel
        );

        super.doInit();
    }

    /**
     * 当此VM的动作为新增的情况时，
     * 调用此方法，将会重置域模型的主键值为null
     */
    @Deprecated
    protected void setNullUid() {
        if (!_wrp.isNew()) {
            M m = getDomainModel();
            if (m != null) m.setUidPk(null);
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
}
