package com.googlecode.easyec.zkoss.ui.builders;

import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

/**
 * 用于构建ZK UI组件的接口类
 *
 * @author junjie
 */
public interface UiBuilder {

    /**
     * 制造一个新的组件的方法
     *
     * @param parameter <code>UiParameter</code>对象
     * @return ZK <code>Component</code>
     */
    Component manufacture(UiParameter parameter);

    /**
     * 查找给定ZK组件绑定的
     * <code>Binder</code>对象实例。
     * 如果给定的组件对象没有绑定并其
     * 父组件也没有绑定任何<code>Binder</code>
     * 对象实例，则返回null
     *
     * @param comp <code>Component</code>
     * @return <code>Binder</code>
     */
    Binder getBinder(Component comp);

    /**
     * 查找给定ZK组件绑定的
     * <code>Binder</code>对象实例。
     * 如果给定的组件对象没有绑定，则
     * 返回null
     *
     * @param comp       <code>Component</code>
     * @param withParent 是否要在父组件中进行查找
     * @return <code>Binder</code>
     */
    Binder getBinder(Component comp, boolean withParent);

    /**
     * 为给定的ZK组件添加注解的方法。
     *
     * @param comp       ZK组件对象
     * @param parameters <code>AnnotationParameter</code>对象
     */
    void addAnnotation(AbstractComponent comp, AnnotationParameter... parameters);

    /**
     * 为给定的ZK组件添加注解的方法。
     *
     * @param comp       ZK组件对象
     * @param withBinder 指出是否要查找<code>Binder</code>对象
     * @param parameters <code>AnnotationParameter</code>对象
     */
    void addAnnotation(AbstractComponent comp, boolean withBinder, AnnotationParameter... parameters);
}
