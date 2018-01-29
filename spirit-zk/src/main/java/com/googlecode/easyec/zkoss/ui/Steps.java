package com.googlecode.easyec.zkoss.ui;

import com.googlecode.easyec.zkoss.ui.builders.DefaultUiParameterBuilder;

import java.util.Map;

/**
 * 表示VM之间跳转的步骤的接口类
 *
 * @author junjie
 */
public interface Steps {

    /**
     * 方法说明参见{@link #stepIn(DefaultUiParameterBuilder)}
     *
     * @param uri ZK page uri
     */
    void stepIn(String uri);

    /**
     * 方法说明参见{@link #stepIn(DefaultUiParameterBuilder)}
     *
     * @param uri  ZK page uri
     * @param args 参数集合
     */
    void stepIn(String uri, Map<Object, Object> args);

    /**
     * 从当前的ZK组件页面步入给定的UI页面参数构造
     * 器中的方法。如果给定的ZK页面组件创建成功，则
     * 当前页面将从当前的父页面组件中脱离出来，
     * 并加载新构造的页面组件。当新构造的页面触发
     * 事件onStepOut时，当前的页面组件才会
     * 被恢复并重新展现出来。
     *
     * @param builder URI参数构造器对象
     */
    void stepIn(DefaultUiParameterBuilder builder);

    /**
     * 从当前ZK页面组件中步出，
     * 并返回到之前的页面的方法
     */
    void stepOut();

    /**
     * 从当前ZK页面组件中步出，
     * 并返回到之前的页面的方法
     *
     * @param data 返回到之前页面的数据对象
     */
    void stepOut(Object data);
}
