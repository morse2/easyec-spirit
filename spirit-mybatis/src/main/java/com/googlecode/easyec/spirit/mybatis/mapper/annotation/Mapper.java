package com.googlecode.easyec.spirit.mybatis.mapper.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {

    /**
     * 域模型的实现类的全名
     *
     * @return 类全名
     */
    String domainModel();
}
