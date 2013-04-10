package com.googlecode.easyec.spirit.web.controller.formbean.factory;

import com.googlecode.easyec.cache.CacheService;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.FormBeansFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-7-21
 * Time: 上午8:20
 * To change this template use File | Settings | File Templates.
 */
public class CacheFormBeansFactory implements FormBeansFactory, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(CacheFormBeansFactory.class);
    public static final String DEFAULT_CACHE_NAME = "formBeansCache";
    private String cacheName = DEFAULT_CACHE_NAME;
    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public boolean addFormBean(String sessionId, FormBean bean) {
        Map<String, FormBean> map = find(sessionId);

        String uri = bean.getUri();
        logger.debug("FormBean's uri: [" + uri + "], bean's token: [" + bean.getToken() +
                "], bean name: [" + bean.getId() + "].");

        if (StringUtils.isBlank(uri)) {
            logger.warn("Form bean's uri is null. Please check it.");

            return false;
        }

        map.put(uri, bean);

        return cacheService.put(cacheName, sessionId, map);
    }

    public FormBean findFormBean(String sessionId, String path) {
        Map<String, FormBean> map = find(sessionId);
        return map.containsKey(path) ? map.get(path) : null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, FormBean> find(String sessionId) {
        Object o = cacheService.get(cacheName, sessionId);

        if (o == null) {
            o = new LinkedHashMap<String, FormBean>(20);
            cacheService.put(cacheName, sessionId, o);
        } else if (!(o instanceof Map)) {
            logger.warn("There is not a Map object in cache. cache name: [" + cacheName +
                    "], key: [" + sessionId + "].");

            o = new LinkedHashMap<String, FormBean>(20);
            cacheService.put(cacheName, sessionId, o);
        }

        return (Map<String, FormBean>) o;
    }

    public boolean removeAll(String sessionId) {
        return cacheService.removeCache(cacheName, sessionId);
    }

    public boolean removeFormBean(String sessionId, String path) {
        Map<String, FormBean> map = find(sessionId);

        if (map.containsKey(path)) {
            boolean b = map.remove(path) != null;

            return cacheService.put(cacheName, sessionId, map) && b;
        }

        return false;
    }

    public void afterPropertiesSet() throws Exception {
        if (cacheService == null) {
            throw new IllegalArgumentException("CacheService object is null.");
        }
    }
}
