package com.googlecode.easyec.spirit.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * 资源配置文件解析类。
 *
 * @author JunJie
 */
public class PropertyResolver {

    private static final Logger logger = LoggerFactory.getLogger(PropertyResolver.class);
    private final Properties properties = new Properties();

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public void init() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] rs = resourcePatternResolver.getResources(path);
        for (Resource r : rs) {
            logger.debug("Resource of property path: [" + r.getFilename() + "].");

            properties.load(r.getInputStream());
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public Integer getInt(String key) {
        try {
            return Integer.valueOf(getString(key));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    public Long getLong(String key) {
        try {
            return Long.valueOf(getString(key));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }
}
