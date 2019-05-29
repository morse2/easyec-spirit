package com.googlecode.easyec.spirit.mybatis.mapper.config;

import com.googlecode.easyec.spirit.mybatis.mapper.annotation.DaoMapperScan;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DaoMapperScannerBeanRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(DaoMapperScan.class.getName()));
        ClassPathDaoMapperScanner scanner = new ClassPathDaoMapperScanner(registry);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        List<String> packagesToScan = new ArrayList<>();
        String[] basePackages = annoAttrs.getStringArray("basePackages");
        if (ArrayUtils.isNotEmpty(basePackages)) {
            Stream.of(basePackages)
                .filter(StringUtils::isNotBlank)
                .forEach(packagesToScan::add);
        }

        Class<?>[] basePackageClasses = annoAttrs.getClassArray("basePackageClasses");
        if (ArrayUtils.isNotEmpty(basePackageClasses)) {
            Stream.of(basePackageClasses)
                .map(ClassUtils::getPackageName)
                .forEach(packagesToScan::add);
        }

        scanner.setSqlSessionFactoryBeanName(annoAttrs.getString("sqlSessionFactoryRef"));
        scanner.registerFilters();
        scanner.scan(org.springframework.util.StringUtils.toStringArray(packagesToScan));
    }
}
