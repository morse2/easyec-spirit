package com.googlecode.easyec.zkoss.utils;

import com.googlecode.easyec.spirit.web.utils.SpringContextUtils;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import javax.servlet.ServletRequest;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * ZK组件支持Spring上下文的工具类。
 *
 * @author JunJie
 */
public final class ZkSpringContextUtils {

    /**
     * 从Spring上下文中获取给定类的实例对象。
     *
     * @param type 类对象类型
     * @param <T>  泛型类型
     * @return 存在于Spring上下文中的对象实例
     */
    public static <T> T getBean(Class<T> type) {
        return getBean0(null, type);
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
        return getBean0(name, type);
    }

    private static <T> T getBean0(String name, Class<T> type) {
        Execution current = Executions.getCurrent();

        if (null == current) {
            Assert.notNull(type, "Class type is null.");

            if (isBlank(name)) return SpringContextUtils.getBean(type);
            else return SpringContextUtils.getBean(name, type);
        }

        Object o = current.getNativeRequest();
        if (o == null || !(o instanceof ServletRequest)) {
            Assert.notNull(type, "Class type is null.");

            if (isBlank(name)) return SpringContextUtils.getBean(type);
            else return SpringContextUtils.getBean(name, type);
        }

        if (isBlank(name)) return SpringContextUtils.getBean((ServletRequest) o, type);
        return SpringContextUtils.getBean((ServletRequest) o, name, type);
    }
}
