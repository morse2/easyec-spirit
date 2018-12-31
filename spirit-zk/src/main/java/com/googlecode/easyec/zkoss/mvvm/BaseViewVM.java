package com.googlecode.easyec.zkoss.mvvm;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;

import java.io.Serializable;

/**
 * 表示视图页面的VM类。
 *
 * @param <T> ZK组件类型
 * @param <M> <code>DomainModel</code>类型
 * @param <E> 主键ID类型
 * @author JunJie.Z
 */
@Init(superclass = true)
@AfterCompose(superclass = true)
public class BaseViewVM<T extends Component, M extends GenericPersistentDomainModel<E>, E extends Serializable> extends BaseFormVM<T, M, E> {

    private static final long serialVersionUID = -542453449086061975L;

    @Override
    final protected M createIfNull() {
        throw new UnsupportedOperationException("Cannot invoke in BaseViewVM.createIfNull()");
    }
}
