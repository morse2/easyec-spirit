package com.googlecode.easyec.spirit.web.controller.formbean.factory;

import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-2
 * Time: 上午12:32
 * To change this template use File | Settings | File Templates.
 */
class DefaultFormBeansFactory implements FormBeansFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFormBeansFactory.class);
    private final Map<String, Map<String, FormBean>> formBeanMap = new LinkedHashMap<String, Map<String, FormBean>>(20);
    private static final Object lock = new Object();

    public boolean addFormBean(String sessionId, FormBean bean) {
        synchronized (lock) {
            Map<String, FormBean> map;
            if (!formBeanMap.containsKey(sessionId)) {
                map = new LinkedHashMap<String, FormBean>(10);

                formBeanMap.put(sessionId, map);
            } else {
                map = formBeanMap.get(sessionId);
            }

            String uri = bean.getUri();
            logger.debug("FormBean's uri: [" + uri + "], bean's token: [" + bean.getToken() +
                    "], bean name: [" + bean.getId() + "].");

            if (StringUtils.isBlank(uri)) {
                logger.warn("Form bean's uri is null. Please check it.");

                return false;
            }

            map.put(uri, bean);

            return true;
        }
    }

    public FormBean findFormBean(String sessionId, String path) {
        synchronized (lock) {
            Map<String, FormBean> beanMap = find(sessionId);

            if (!beanMap.containsKey(path)) {
                logger.debug("cannot find FormBean, path: [" + path + "].");

                return null;
            }

            return beanMap.get(path);
        }
    }

    public Map<String, FormBean> find(String sessionId) {
        synchronized (lock) {
            Map<String, FormBean> map;
            if (!formBeanMap.containsKey(sessionId)) {
                map = new LinkedHashMap<String, FormBean>(10);
            } else {
                map = formBeanMap.get(sessionId);
            }

            return map;
        }
    }

    public boolean removeAll(String sessionId) {
        if (formBeanMap.containsKey(sessionId)) {
            formBeanMap.clear();
            return true;
        }

        logger.debug("FormBean map doesn't consist of session id. [" + sessionId + "].");
        return false;
    }

    public boolean removeFormBean(String sessionId, String path) {
        if (formBeanMap.containsKey(sessionId)) {
            Map<String, FormBean> beanMap = formBeanMap.get(sessionId);

            if (beanMap.containsKey(path)) {
                return beanMap.remove(path) != null;
            }

            logger.debug("FormBean map doesn't consist of path. [" + path + "].");
            return false;
        }

        logger.debug("FormBean map doesn't consist of session id. [" + sessionId + "].");
        return false;
    }
}
