package com.googlecode.easyec.spirit.web.controller.formbean.annotations;

import java.lang.annotation.*;

/**
 * 搜索条件的类型注解
 *
 * @author JunJie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchTermType {

    /**
     * 搜索条件转换实现类可接受的参数类型
     *
     * @return 对象类型
     */
    Class<?>[] value();
}
