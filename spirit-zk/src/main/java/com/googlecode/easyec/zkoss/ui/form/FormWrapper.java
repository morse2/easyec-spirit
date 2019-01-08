package com.googlecode.easyec.zkoss.ui.form;

import com.googlecode.easyec.spirit.domain.GenericPersistentDomainModel;

import java.io.Serializable;

/**
 * 表单域模型的封装类。
 * 此类封装了域模型对象实例
 * 及表单的状态（新增、编辑）
 *
 * @param <T>  域模型对象
 * @param <PK> 域模型主键类型对象
 * @author junjie
 */
public interface FormWrapper<T extends GenericPersistentDomainModel<PK>, PK extends Serializable> extends Serializable {

    /**
     * 返回当前的域模型对象实例
     *
     * @return <code>DomainModel</code>
     */
    T getDomainModel();

    /**
     * 表示当前域模型是否是一个新的对象。
     * 新对象意思为还未进行数据层的持久化
     * 操作
     *
     * @return bool值
     */
    boolean isNew();
}
