package com.googlecode.easyec.spirit.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

/**
 * 支持Spring上下文操作的工具类。
 *
 * @author JunJie
 */
public final class SpringContextUtils implements ApplicationContextAware, BeanFactoryAware, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);

    /* global variable */
    private static final SpringContextUtils instance = new SpringContextUtils();

    /* local variables here */
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    /**
     * 判断该工具类是否已经被Spring实例化
     *
     * @return bool值
     */
    public static boolean isInitialized() {
        synchronized (instance) {
            return instance.applicationContext != null
                && instance.beanFactory != null;
        }
    }

    /**
     * 从Spring上下文中获取给定类的实例对象。
     *
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    public static <T> T getBean(Class<T> type) {
        synchronized (instance) {
            return instance.applicationContext.getBean(type);
        }
    }

    /**
     * 从Spring上下文中获取给定名称的实例对象。
     *
     * @param name Spring定义的Bean名字
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    public static <T> T getBean(String name, Class<T> type) {
        synchronized (instance) {
            return instance.applicationContext.getBean(name, type);
        }
    }

    /**
     * 从当前HTTP请求中获取Spring上下文里的实例对象。
     *
     * @param req  当前HTTP请求
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    public static <T> T getBean(HttpServletRequest req, Class<T> type) {
        return RequestContextUtils.findWebApplicationContext(req).getBean(type);
    }

    /**
     * 从当前HTTP请求中获取Spring上下文里的实例对象。
     *
     * @param req  当前HTTP请求
     * @param name Spring定义的Bean名字
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    public static <T> T getBean(HttpServletRequest req, String name, Class<T> type) {
        return RequestContextUtils.findWebApplicationContext(req).getBean(name, type);
    }

    /**
     * 从当前HTTP请求中获取Spring上下文里的实例对象。
     *
     * @param sr   当前HTTP请求
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    @Deprecated
    public static <T> T getBean(ServletRequest sr, Class<T> type) {
        return RequestContextUtils.getWebApplicationContext(sr).getBean(type);
    }

    /**
     * 从当前HTTP请求中获取Spring上下文里的实例对象。
     *
     * @param sr   当前HTTP请求
     * @param name Spring定义的Bean名字
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    @Deprecated
    public static <T> T getBean(ServletRequest sr, String name, Class<T> type) {
        return RequestContextUtils.getWebApplicationContext(sr).getBean(name, type);
    }

    /**
     * 为给定的Bean对象实例实现自动织入属性变量的方法。
     * <p>
     * 此方法默认以{@link AutowireCapableBeanFactory#AUTOWIRE_BY_TYPE}
     * 方式注入Bean的属性，并且指出是否对Bean属性进行依赖检查
     * </p>
     *
     * @param bean            Bean对象
     * @param dependencyCheck 是否对Bean属性进行依赖检查
     */
    public static void autowireBeanProperties(Object bean, boolean dependencyCheck) {
        autowireBeanProperties(bean, AUTOWIRE_BY_TYPE, dependencyCheck);
    }

    /**
     * 为给定的Bean对象实例实现自动织入属性变量的方法。
     *
     * @param bean            Bean对象
     * @param autowireType    Bean属性织入方式
     * @param dependencyCheck 是否对Bean属性进行依赖检查
     */
    public static void autowireBeanProperties(Object bean, int autowireType, boolean dependencyCheck) {
        synchronized (instance) {
            if (instance.beanFactory instanceof AutowireCapableBeanFactory) {
                ((AutowireCapableBeanFactory) instance.beanFactory)
                    .autowireBeanProperties(bean, autowireType, dependencyCheck);
            } else {
                logger.warn("BeanFactory object isn't instance of AutowireCapableBeanFactory." +
                    " Actual type: [{}].", instance.beanFactory.getClass().getName());
            }
        }
    }

    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance.applicationContext = applicationContext;
    }

    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        instance.beanFactory = beanFactory;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(instance.applicationContext, "ApplicationContext object cannot be null.");
        Assert.notNull(instance.beanFactory, "BeanFactory object cannot be null.");
    }
}
