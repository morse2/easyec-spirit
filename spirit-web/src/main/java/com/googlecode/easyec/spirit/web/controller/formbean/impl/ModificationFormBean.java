package com.googlecode.easyec.spirit.web.controller.formbean.impl;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-4-24
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class ModificationFormBean extends AbstractFormBean {

    private static final long serialVersionUID = 1793113636321840326L;

    public FormType getFormType() {
        return FormType.MODIFY_FORM;
    }

    /**
     * 将当前表单内容转换成给定的类对象定义
     *
     * @param c   类型为<strong>T</strong>的类定义
     * @param <T> 泛型类型定义
     * @return <code>T</code>
     */
    abstract public <T> T convertTo(Class<T> c);
}
