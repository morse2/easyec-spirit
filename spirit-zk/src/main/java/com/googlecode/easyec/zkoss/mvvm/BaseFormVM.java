package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.PersistentDomainModel;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.Assert;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import java.util.Map;

import static org.zkoss.zk.ui.Executions.getCurrent;

/**
 * 基于表单的模型视图-视图模型基本类。
 * 此基类提供了当前操作是新增还是编辑的标记。
 * <p>
 * 实现方式为：{@code Executions.getCurrent().getArg()}来获取变量
 * {@link #ARG_FORM_OBJECT}，如果变量不为null，并且对象实例来自于
 * {@link PersistentDomainModel}，并且 {@link PersistentDomainModel#getUidPk()}
 * 不为空，则判断为此操作为编辑；否则判断为新增。
 * </p>
 *
 * @author JunJie
 */
@AfterCompose(superclass = true)
public abstract class BaseFormVM<T extends Component, M extends PersistentDomainModel> extends BaseVM<T> {

    /**
     * 当前线程中的表单参数的Key。
     */
    public static final  String ARG_FORM_OBJECT  = "com.googlecode.easyec.zkoss.mvvm.FormObject";
    private static final long   serialVersionUID = -7906315687396164649L;

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
     * 初始化方法。
     */
    @Init
    @SuppressWarnings("unchecked")
    public void initBaseFormVM() {
        Map<?, ?> arg = getCurrent().getArg();

        if (MapUtils.isEmpty(arg) || !arg.containsKey(ARG_FORM_OBJECT)) {
            domainModel = createIfNull();
            action = FormAction.INSERT;
        } else {
            Object o = arg.get(ARG_FORM_OBJECT);

            if (null == o) {
                domainModel = createIfNull();
                action = FormAction.INSERT;
            } else {
                Assert.isInstanceOf(PersistentDomainModel.class, o);

                if (null == ((PersistentDomainModel) o).getUidPk()) {
                    domainModel = createIfNull();
                    action = FormAction.INSERT;
                } else {
                    domainModel = loadModel((M) o);
                    action = FormAction.UPDATE;
                }
            }
        }
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
