package com.googlecode.easyec.spirit.mybatis.mapper.config;

import com.googlecode.easyec.spirit.mybatis.mapper.annotation.Mapper;
import com.googlecode.easyec.spirit.mybatis.mapper.support.DaoMapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;

class ClassPathDaoMapperScanner extends ClassPathBeanDefinitionScanner {

    private DaoMapperFactoryBean<?> mapperFactoryBean = new DaoMapperFactoryBean<>();

    private String sqlSessionFactoryBeanName;

    void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    ClassPathDaoMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    void registerFilters() {
        addIncludeFilter((reader, factory) -> reader.getAnnotationMetadata().hasAnnotation(Mapper.class.getName()));

        // exclude package-info.java
        addExcludeFilter((reader, factory) -> {
            String className = reader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
//        List<Class<?>> classes = new ArrayList<>();

        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
//            String beanClassName = definition.getBeanClassName();
//            Class<?> beanClass = getBeanClass(beanClassName);

            /*classes.stream().filter(cls -> {
                System.out.println(cls);
                return true;
            });

            classes.add(beanClass);*/

            if (logger.isDebugEnabled()) {
                logger.debug("Creating DaoMapperFactoryBean with name '" + holder.getBeanName()
                    + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(this.mapperFactoryBean.getClass());

            boolean explicitFactoryUsed = false;
            if (StringUtils.hasText(this.sqlSessionFactoryBeanName)) {
                definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(this.sqlSessionFactoryBeanName));
                explicitFactoryUsed = true;
            }

            if (!explicitFactoryUsed) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Enabling autowire by type for DaoMapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }

                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            }
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    private Class<?> getBeanClass(String beanClassName) {
        try {
            return ClassUtils.forName(beanClassName, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }
}
