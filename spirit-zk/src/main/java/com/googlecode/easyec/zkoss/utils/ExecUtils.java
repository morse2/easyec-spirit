package com.googlecode.easyec.zkoss.utils;

import org.apache.commons.collections4.MapUtils;
import org.zkoss.zk.ui.Executions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.googlecode.easyec.zkoss.utils.ExecUtils.FindScope.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        return request instanceof HttpServletRequest
            ? (HttpServletRequest) request
            : null;
    }

    /**
     * 获取当前<code>HttpServletResponse</code>对象实例
     *
     * @return <code>HttpServletResponse</code>
     */
    public static HttpServletResponse getNativeResponse() {
        Object response = Executions.getCurrent().getNativeResponse();
        return response instanceof HttpServletResponse
            ? ((HttpServletResponse) response)
            : null;
    }

    /**
     * 查找参数值的方法，如果搜索范围提供的时All，
     * 那么该方法会按照下面的顺序来查找
     * 当前VM中设置的参数值。<br/>
     * <ol>
     * <li>当前<code>Execution</code>的Arg范围</li>
     * <li>当前<code>Execution</code>范围</li>
     * <li>当前<code>Session</code>范围</li>
     * </ol>
     *
     * @param key   参数KEY
     * @param scope <code>FindScope</code>
     * @param type  返回的对象类型
     * @param <R>   返回的对象类型枚举
     * @return 参数KEY对应的值
     */
    public static <R> R findParam(String key, FindScope scope, Class<R> type) {
        if (isBlank(key) || scope == null || type == null) return null;

        R ret = null;
        switch (scope) {
            case Arg:
                Map<?, ?> args = Executions.getCurrent().getArg();
                Object _argVal
                    = MapUtils.isNotEmpty(args)
                    && args.containsKey(key)
                    ? args.get(key)
                    : null;

                if (_argVal != null) {
                    ret = type.cast(_argVal);
                }

                break;
            case Execution:
                Object _execVal = Executions.getCurrent().getAttribute(key);
                if (_execVal != null) {
                    ret = type.cast(_execVal);
                }

                break;
            case Session:
                Object _sessVal = Executions.getCurrent().getSession().getAttribute(key);
                if (_sessVal != null) {
                    ret = type.cast(_sessVal);
                }

                break;
            case All:
                ret = findParam(key, Arg, type);
                if (ret != null) break;
                ret = findParam(key, Execution, type);
                if (ret != null) break;
                ret = findParam(key, Session, type);
        }

        return ret;
    }

    /**
     * 标识查询参数值范围的枚举类
     */
    public static enum FindScope {

        /**
         * All scopes
         */
        All,
        /**
         * Execution.Arg
         */
        Arg,
        /**
         * Execution
         */
        Execution,
        /**
         * Session
         */
        Session
    }
}
