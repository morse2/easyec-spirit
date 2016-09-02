package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.domain.PersistentDomainModel;
import com.googlecode.easyec.spirit.web.utils.BeanUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static com.googlecode.easyec.zkoss.mvvm.BaseVM.FindScope.All;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.zkoss.zk.ui.Executions.getCurrent;

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
 * {@link #ARG_FORM_OBJECT}，如果变量不为null，并且对象实例来自于
 * {@link PersistentDomainModel}，并且 {@link PersistentDomainModel#getUidPk()}
 * 不为空，则判断为此操作为编辑；否则判断为新增。
 * 注意：如果对象信息是从Session范围中获取，则获取变量之后即将该变量从Session范围中删除。
 * </p>
 *
 * @author JunJie
 */
@AfterCompose(superclass = true)
public abstract class BaseFormVM<T extends Component, M extends GenericPersistentDomainModel<E>, E extends Serializable> extends BaseVM<T> {

    /**
     * 表单参数对象的KEY
     */
    public static final String ARG_FORM_OBJECT = "com.googlecode.easyec.zkoss.mvvm.FormObject";
    /**
     * 标识是否校验表单对象的主键为空
     */
    public static final String ARG_CHECK_UIDPK = "com.googlecode.easyec.zkoss.mvvm.CheckUidPK";
    /**
     * 标识是否匹配VM对象类型
     */
    public static final String ARG_MATCH_VM = "com.googlecode.easyec.zkoss.mvvm.MatchVM";
    private static final long serialVersionUID = -8030091277630808636L;

    /**
     * 域模型对象实例。此对象不能为空。
     */
    protected M          domainModel;
    protected FormAction action;

    private String preQs;

    /**
     * 返回得到当前VM对象中定义的域模型对象
     *
     * @return <code>PersistentDomainModel</code>对象实例
     */
    public M getDomainModel() {
        return domainModel;
    }

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
     * 创建一个持久化域模型对象实例。
     * 此方法只有当获取不到参数
     * {@code ARG_FORM_OBJECT}的时候，
     * 才会被使用。
     *
     * @return 域模型对象实例
     */
    abstract public M createIfNull();

    /**
     * 初始化加载域模型对象实例。
     *
     * @param model 当前参数传递的域模型对象
     * @return 加载完后的域模型对象实例
     */
    abstract public M loadModel(M model);

    /**
     * 初始化方法
     */
    @Init(superclass = true)
    public void initBaseFormVM() {
        preQs = ((HttpServletRequest) getCurrent().getNativeRequest()).getQueryString();

        resolveFormVariable();
    }

    @SuppressWarnings("unchecked")
    protected void resolveFormVariable() {
        Object var = findParameter(ARG_FORM_OBJECT, All, GenericPersistentDomainModel.class);

        if (null != var) {
            // ARG_FORM_OBJECT参数不为空，执行下面的逻辑
            /*
             * 获取是否要校验表单对象主键的参数，
             * 如果用户没给定该参数值，
             * 那么默认则认为需要做表单对象的校验
             */
            Boolean shouldCheck = findParameter(ARG_CHECK_UIDPK, All, Boolean.class);
            if (null == shouldCheck) shouldCheck = true;

            // 执行校验是否应该进入的逻辑
            if (!shouldCheck || null != ((GenericPersistentDomainModel) var).getUidPk()) {
                Class cls = BeanUtils.findGenericType(this, BaseFormVM.class, 1);

                if (null == cls) {
                    action = FormAction.UPDATE;
                    domainModel = loadModel((M) var);

                    return;
                }

                /*
                 * 获取匹配的VM类型的参数值，
                 * 该参数用来表示获取到的表单对象
                 * 是否用于给定的VM类
                 */
                Class matchesVM = findParameter(ARG_MATCH_VM, All, Class.class);
                boolean b = (null == matchesVM || matchesVM.isInstance(this));

                if (b && cls.isInstance(var)) {
                    action = FormAction.UPDATE;
                    domainModel = loadModel((M) var);

                    return;
                }

                logger.warn(
                    "Current form object's type isn't instanceof [{}], actual type: [{}].",
                    cls.getName(), var.getClass().getName()
                );
            }
        }

        /*
         * 进入不了loadModel方法，
         * 那么默认进入createIfNull方法
         */
        action = FormAction.INSERT;
        domainModel = createIfNull();
    }

    /**
     * 当此VM的动作为新增的情况时，
     * 调用此方法，将会重置域模型的主键值为null
     */
    protected void setNullUid() {
        switch (action) {
            case INSERT:
                domainModel.setUidPk(null);
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
     * 表单动作类。此类表示了当前表单操作是新增还是编辑。
     */
    public static enum FormAction {

        /**
         * 标识当前为新增操作
         */
        INSERT,
        /**
         * 标识当前为更新操作
         */
        UPDATE,
    }
}
