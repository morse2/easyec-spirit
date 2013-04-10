package com.googlecode.easyec.spirit.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletRequest;

/**
 * 支持Spring上下文操作的工具类。
 *
 * @author JunJie
 */
public final class SpringContextUtils implements ApplicationContextAware {

    private static final SpringContextUtils instance = new SpringContextUtils();
    private ApplicationContext applicationContext;

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
     * @param sr   当前HTTP请求
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
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
    public static <T> T getBean(ServletRequest sr, String name, Class<T> type) {
        return RequestContextUtils.getWebApplicationContext(sr).getBean(name, type);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance.applicationContext = applicationContext;
    }
}
