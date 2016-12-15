package com.googlecode.easyec.spirit.web.controller.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置请求的URL为可重用的拦截器。
 *
 * @author JunJie
 */
public class RequestUriReusingInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestUriReusingInterceptor.class);

    /**
     * 表示当前正在访问的uri路径
     */
    public static final String THIS_REQUEST_URI = RequestUriReusingInterceptor.class.getName() + ".REQUEST_URI";
    /**
     * 表示进入当前uri路径的引用的uri路径
     */
    public static final String PREV_REQUEST_URI = RequestUriReusingInterceptor.class.getName() + ".PREVIOUS_URI";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof Controller) {
            _resolveRefererUri(request);
            _resolveRequestUri(request);

            return true;
        }

        if (handler.getClass().isAnnotationPresent(org.springframework.stereotype.Controller.class)) {
            _resolveRefererUri(request);
            _resolveRequestUri(request);

            return true;
        }

        if (handler instanceof HandlerMethod) {
            Class<?> beanType = ((HandlerMethod) handler).getBeanType();
            if (beanType.isAnnotationPresent(org.springframework.stereotype.Controller.class)) {
                _resolveRefererUri(request);
                _resolveRequestUri(request);

                return true;
            }
        }

        return true;
    }

    /* 解析请求的URI */
    private void _resolveRequestUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI().replaceFirst(request.getContextPath(), "");
        logger.debug("This uri is: [" + requestUri + "].");

        request.setAttribute(THIS_REQUEST_URI, requestUri);
    }

    /* 解析引用的URI */
    private void _resolveRefererUri(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            logger.debug("Referer URL: [{}].", referer);

            String serverName = request.getServerName();
            logger.debug("Current server name: [{}].", serverName);

            int port = request.getServerPort();
            logger.debug("Current server port: [{}].", port);

            String scheme = request.getScheme();
            logger.debug("Current scheme: [{}].", scheme);

            String serverRegex = new StringBuffer()
                .append(scheme)
                .append("\\://")
                .append(serverName)
                .toString();
            logger.debug("Server regex: [{}].", serverRegex);

            referer = referer.replaceAll(serverRegex, "");
            if (referer.startsWith(":")) {
                String portRegex = new StringBuffer()
                    .append("\\:")
                    .append(port)
                    .toString();

                referer = referer.replaceAll(portRegex, "");
            }

            logger.debug("New Referer: [{}].", referer);

            referer = referer.replaceFirst(request.getContextPath(), "");
            logger.debug("Referer uri without context path: [{}].", referer);

            int i = referer.indexOf("?");
            if (i > 0) {
                referer = referer.substring(0, i);
                logger.debug("Remove referer uri's query sting parameters.");
            }

            request.setAttribute(PREV_REQUEST_URI, referer);
            logger.debug("Set the previous URI done. [{}].", referer);
        }
    }
}
