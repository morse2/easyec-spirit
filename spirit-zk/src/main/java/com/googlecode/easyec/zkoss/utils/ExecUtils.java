package com.googlecode.easyec.zkoss.utils;

import org.zkoss.zk.ui.Executions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于<code>Execution</code>对象实例
 * 操作的工具类
 *
 * @author junjie
 */
public class ExecUtils {

    private ExecUtils() {}

    /**
     * 获取当前<code>HttpServletRequest</code>对象实例
     *
     * @return <code>HttpServletRequest</code>
     */
    public static HttpServletRequest getNativeRequest() {
        Object request = Executions.getCurrent().getNativeRequest();
        if (request != null && request instanceof HttpServletRequest)
            return (HttpServletRequest) request;
        return null;
    }

    /**
     * 获取当前<code>HttpServletResponse</code>对象实例
     *
     * @return <code>HttpServletResponse</code>
     */
    public static HttpServletResponse getNativeResponse() {
        Object response = Executions.getCurrent().getNativeResponse();
        if (response != null && response instanceof HttpServletResponse)
            return (HttpServletResponse) response;
        return null;
    }
}
