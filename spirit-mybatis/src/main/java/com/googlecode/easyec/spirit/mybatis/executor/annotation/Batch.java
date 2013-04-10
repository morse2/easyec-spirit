package com.googlecode.easyec.spirit.mybatis.executor.annotation;

import java.lang.annotation.*;

/**
 * 执行批量处理的注解类
 *
 * @author JunJie
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Batch {

}
