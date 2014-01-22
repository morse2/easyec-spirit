package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.domain.PersistentDomainModel;
import com.googlecode.easyec.spirit.utils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;
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
public abstract class BaseFormVM<T extends Component, M extends PersistentDomainModel> extends BaseVM<T> {

    /**
     * 表单参数对象的KEY
     */
    public static final  String ARG_FORM_OBJECT  = "com.googlecode.easyec.zkoss.mvvm.FormObject";
    /**
     * 表单参数指向的URI的KEY
     */
    public static final  String ARG_REQUEST_URI  = "com.googlecode.easyec.zkoss.mvvm.ZKRequestURI";
    /**
     * 标识是否校验表单对象的主键为空
     */
    public static final  String ARG_CHECK_UIDPK  = "com.googlecode.easyec.zkoss.mvvm.CheckUidPK";
    /**
     * 标识是否匹配VM对象类型
     */
    public static final  String ARG_MATCH_VM     = "com.googlecode.easyec.zkoss.mvvm.MatchVM";
    private static final long   serialVersionUID = 8575235562676085828L;

    /**
     * 域模型对象实例。此对象不能为空。
     */
    protected M          domainModel;
    protected FormAction action;

    /**
     * 返回得到当前VM对象中定义的域模型对象
     *
     * @return <code>PersistentDomainModel</code>对象实例
     */
    public M getDomainModel() {
        return domainModel;
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
        resolveFormVariable();
    }

    @SuppressWarnings("unchecked")
    private void resolveFormVariable() {
        Object var;
        Boolean shouldCheck = true;
        Class matchesVM = null;
        Map<?, ?> arg = getCurrent().getArg();
        if (MapUtils.isNotEmpty(arg) && arg.containsKey(ARG_FORM_OBJECT)) {
            var = arg.get(ARG_FORM_OBJECT);
        } else {
            Session session = getCurrent().getSession();
            // 从session中获取表单对象
            var = session.getAttribute(ARG_FORM_OBJECT);
            // 校验是否需要验证模型对象为空
            Boolean b = (Boolean) session.getAttribute(ARG_CHECK_UIDPK);
            if (null != b) shouldCheck = b;
            // 从session中获取此表单请求的URI
            String uri = (String) session.getAttribute(ARG_REQUEST_URI);
            // 从session中获取此表单匹配VM参数
            matchesVM = (Class) session.getAttribute(ARG_MATCH_VM);
            // 如果URI参数为空，认为这是ajax提交的请求，则将表单对象remove掉
            if (isBlank(uri)) {
                session.removeAttribute(ARG_FORM_OBJECT);
                session.removeAttribute(ARG_CHECK_UIDPK);
            }
        }

        if (null != var) {
            Assert.isInstanceOf(GenericPersistentDomainModel.class, var);

            if (!shouldCheck || null != ((GenericPersistentDomainModel) var).getUidPk()) {
                boolean b = (null == matchesVM || matchesVM.isInstance(this));
                Class cls = BeanUtils.getGenericType(this, 1);

                if (null == cls) {
                    action = FormAction.UPDATE;
                    domainModel = loadModel((M) var);

                    return;
                }

                if (cls.isInstance(var) && b) {
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
