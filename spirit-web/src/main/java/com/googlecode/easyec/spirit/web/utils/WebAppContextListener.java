package com.googlecode.easyec.spirit.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-18
 * Time: 下午11:46
 * To change this template use File | Settings | File Templates.
 */
public class WebAppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(WebAppContextListener.class);

    public static final String ATTACH_PATH = "attachPath";
    public static final String ATTACH_TYPE = "attachType";
    public static final String ATTACH_SIZE = "attachSize";

    public void contextInitialized(ServletContextEvent sce) {
        String fullPath = sce.getServletContext().getRealPath("/");
        logger.debug("real path: [" + fullPath + "].");

        String attachPath = sce.getServletContext().getInitParameter(ATTACH_PATH);
        logger.debug("attached path: [" + attachPath + "].");

        String attachType = sce.getServletContext().getInitParameter(ATTACH_TYPE);
        logger.debug("attached type: [" + attachType + "].");

        String attachSize = sce.getServletContext().getInitParameter(ATTACH_SIZE);
        logger.debug("attached size: [" + attachSize + "].");

        WebAppInfo.instantiate(fullPath, attachPath, attachType, attachSize);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // no op
    }
}
