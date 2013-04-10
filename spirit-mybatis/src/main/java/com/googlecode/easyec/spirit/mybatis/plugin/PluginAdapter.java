package com.googlecode.easyec.spirit.mybatis.plugin;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

/**
 * Mybatis插件适配器类。
 * 此类默认实现了{@link #plugin(Object)}
 * 和{@link #setProperties(java.util.Properties)}
 * 两个方法。
 *
 * @author JunJie
 */
public abstract class PluginAdapter implements Interceptor {

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        // no op
    }
}
