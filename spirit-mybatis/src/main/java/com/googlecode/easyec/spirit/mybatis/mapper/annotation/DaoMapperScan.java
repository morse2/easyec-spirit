package com.googlecode.easyec.spirit.mybatis.mapper.annotation;

import com.googlecode.easyec.spirit.mybatis.mapper.config.DaoMapperScannerBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Import(DaoMapperScannerBeanRegistrar.class)
public @interface DaoMapperScan {

    String[] basePackages() default { };

    Class<?>[] basePackageClasses() default { };

    String sqlSessionFactoryRef() default "";
}
