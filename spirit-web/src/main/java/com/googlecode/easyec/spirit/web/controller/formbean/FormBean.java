package com.googlecode.easyec.spirit.web.controller.formbean;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANG78
 * Date: 12-4-24
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 */
public interface FormBean extends Serializable {

    public static enum FormType {

        /**
         * 创建页面表单
         */
        CREATE_FORM,
        /**
         * 修改页面表单
         */
        MODIFY_FORM,
        /**
         * 搜索页面表单
         */
        SEARCH_FORM
    }

    public static enum FormMethod {

        /**
         * 表单提交方法为GET
         */
        GET,
        /**
         * 表单提交方法为POST
         */
        POST
    }

    /**
     * 返回当前表单的类型
     *
     * @return 创建、修改、搜索中的一种
     * @see FormType
     */
    FormType getFormType();

    /**
     * 返回表单的Token值
     *
     * @return
     */
    String getToken();

    /**
     * 返回当前表单的id
     *
     * @return
     */
    String getId();

    /**
     * 返回当前表单提交的uri
     *
     * @return
     */
    String getUri();

    /**
     * 返回表单提交的方法
     *
     * @return
     * @see FormType
     */
    FormMethod getFormMethod();
}
