package com.googlecode.easyec.spirit.web.controller.formbean.annotations;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-4-27
 * Time: 下午12:45
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Form {

    /**
     * 返回此表单在使用时的名字。
     *
     * @return 表单的名称
     */
    String value();
}
