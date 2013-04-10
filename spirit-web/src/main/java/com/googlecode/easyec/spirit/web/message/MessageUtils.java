package com.googlecode.easyec.spirit.web.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 为Servlet请求返回消息至页面提供缓存的工具类。
 *
 * @author JunJie
 */
public class MessageUtils {

    private static final String $MESSAGE_SUFFIX$ = "$MESSAGE$SUFFIX$";
    private static final String $ERROR_SUFFIX$ = "$ERROR_SUFFIX$";

    /**
     * 为当前的请求存储一条消息。
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     * @param message 一条消息，可以是国际化的key，也可以是具体的信息内容
     */
    public static void saveMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        String messageKey = session.getId() + $MESSAGE_SUFFIX$;
        session.setAttribute(messageKey, message);
    }

    /**
     * 为当前的请求存储一条错误消息。
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     * @param error   一条消息，可以是国际化的key，也可以是具体的信息内容
     */
    public static void saveError(HttpServletRequest request, String error) {
        HttpSession session = request.getSession();
        String messageKey = session.getId() + $ERROR_SUFFIX$;
        session.setAttribute(messageKey, error);
    }

    /**
     * 从当前请求中获取之前存入的消息。
     * <p>
     * 在调用此方法后，之前的消息也会随之被删除。
     * </p>
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     * @return 消息信息
     */
    public static String getMessage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String messageKey = session.getId() + $MESSAGE_SUFFIX$;
        try {
            return (String) session.getAttribute(messageKey);
        } finally {
            session.removeAttribute(messageKey);
        }
    }

    /**
     * 从当前请求中获取之前存入错误消息。
     * <p>
     * 在调用此方法后，之前的消息也会随之被删除。
     * </p>
     *
     * @param request {@link javax.servlet.http.HttpServletRequest}
     * @return 消息信息
     */
    public static String getError(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String messageKey = session.getId() + $ERROR_SUFFIX$;
        try {
            return (String) session.getAttribute(messageKey);
        } finally {
            session.removeAttribute(messageKey);
        }
    }
}
