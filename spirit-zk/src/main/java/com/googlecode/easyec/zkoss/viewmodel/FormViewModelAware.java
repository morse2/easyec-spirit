package com.googlecode.easyec.zkoss.viewmodel;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import com.googlecode.easyec.spirit.web.utils.BeanUtils;
import com.googlecode.easyec.zkoss.ui.form.FormWrapper;
import com.googlecode.easyec.zkoss.ui.form.functions.EditOperator;
import com.googlecode.easyec.zkoss.ui.form.functions.NewOperator;
import com.googlecode.easyec.zkoss.ui.form.impl.DefaultFormWrapper;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Component;

import java.io.Serializable;

import static com.googlecode.easyec.zkoss.utils.ExecUtils.FindScope.All;
import static com.googlecode.easyec.zkoss.utils.ExecUtils.findParam;

/**
 * 表单视图-模型的基类。
 * 该类提供了ZK表单操作的一般方法
 *
 * @param <T> ZK组件对象
 * @param <M> 持久化模型对象
 * @param <E> 持久化模型的主键类型对象
 * @author junjie
 */
public interface FormViewModelAware<T extends Component, M extends GenericPersistentDomainModel<E>, E extends Serializable> extends ViewModelAware<T> {

    /**
     * 表单参数对象的KEY
     */
    String ARG_FORM_OBJECT = "com.googlecode.easyec.zkoss.mvvm.FormObject";
    /**
     * 标识是否校验表单对象的主键为空
     */
    String ARG_CHECK_UIDPK = "com.googlecode.easyec.zkoss.mvvm.CheckUidPK";
    /**
     * 标识是否匹配VM对象类型
     */
    String ARG_MATCH_VM = "com.googlecode.easyec.zkoss.mvvm.MatchVM";

    /**
     * 返回当前的域模型对象实例
     *
     * @return <code>DomainModel</code>
     */
    M getDomainModel();

    /**
     * 判断表单的域模型状态是不是新增或编辑
     *
     * @return bool值
     */
    boolean isNew();

    /**
     * 返回上一个页面传递过来的查询字符串
     *
     * @return query string
     */
    String getPreQs();

    /**
     * 初始化表单视图-模型的表单数据的方法
     *
     * @param newOp  <code>NewOperator</code>对象
     * @param editOp <code>EditOperator</code>对象
     * @return <code>FormWrapper</code>对象
     */
    @SuppressWarnings("unchecked")
    default FormWrapper<M, E> initialize(NewOperator<M, E> newOp, EditOperator<M, E> editOp) {
        Assert.notNull(newOp, "NewOperator cannot be null.");
        Assert.notNull(editOp, "EditOperator cannot be null.");

        Object var = findParam(ARG_FORM_OBJECT, All, GenericPersistentDomainModel.class);

        if (null != var) {
            // ARG_FORM_OBJECT参数不为空，执行下面的逻辑
            /*
             * 获取是否要校验表单对象主键的参数，
             * 如果用户没给定该参数值，
             * 那么默认则认为需要做表单对象的校验
             */
            Boolean shouldCheck = findParam(ARG_CHECK_UIDPK, All, Boolean.class);
            if (null == shouldCheck) shouldCheck = true;

            // 执行校验是否应该进入的逻辑
            if (!shouldCheck || null != ((GenericPersistentDomainModel) var).getUidPk()) {
                Class cls = BeanUtils.findGenericType(this, getClass(), 1);

                if (null == cls) {
                    M _ret = editOp.perform((M) var);
                    if (_ret != null) {
                        return new DefaultFormWrapper<>(_ret);
                    }

                    /*
                     * 如果editOp.perform(M)方法未能正确返回domainModel，
                     * 则进入createIfNull方法。
                     */
                    return new DefaultFormWrapper<>(newOp.perform());
                }

                /*
                 * 获取匹配的VM类型的参数值，
                 * 该参数用来表示获取到的表单对象
                 * 是否用于给定的VM类
                 */
                Class matchesVM = findParam(ARG_MATCH_VM, All, Class.class);
                boolean b = (null == matchesVM || matchesVM.isInstance(this));

                if (b && cls.isInstance(var)) {
                    return new DefaultFormWrapper<>(
                        editOp.perform((M) var)
                    );
                }
            }
        }

        /*
         * 进入不了editOp.perform方法，
         * 那么默认进入createIfNull方法
         */
        return new DefaultFormWrapper<>(newOp.perform());
    }
}
