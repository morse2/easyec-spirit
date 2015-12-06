package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 资源配置文件解析类。
 *
 * @author JunJie
 */
public class PropertyResolver implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PropertyResolver.class);
    private org.springframework.core.env.PropertyResolver propertyResolver;
    private boolean ignoreResourceNotFound = true;
    private Resource[] locations;
    private String fileEncoding;

    /**
     * 设置资源位置
     *
     * @param locations 资源文件的位置集合
     */
    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

    /**
     * 指出是否忽略资源没找到的异常
     *
     * @param ignoreResourceNotFound bool值
     */
    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }

    /**
     * 设置资源文件的编码
     *
     * @param fileEncoding 文件编码
     */
    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getString(String key) {
        return getString(key, new Object[0]);
    }

    public String getString(String key, Object... params) {
        return _formatStr(getProperty(key, String.class), params);
    }

    public Integer getInt(String key) {
        return getProperty(key, Integer.class);
    }

    public Long getLong(String key) {
        return getProperty(key, Long.class);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        return propertyResolver != null && propertyResolver.containsProperty(key)
            ? propertyResolver.getProperty(key, targetType)
            : null;
    }

    /* 格式化匹配的参数值 */
    private String _formatStr(String val, Object... params) {
        if (StringUtils.isBlank(val)) return null;
        if (ArrayUtils.isEmpty(params)) return val;

        String s = val;
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (null == p) p = "";

            s = s.replaceAll("\\{" + i + "}", p.toString());
        }

        return s;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.locations != null) {
            Properties props = new Properties();
            for (Resource location : this.locations) {
                if (logger.isInfoEnabled()) {
                    logger.info("Loading properties file from " + location);
                }

                try {
                    PropertiesLoaderUtils.fillProperties(
                        props, new EncodedResource(location, this.fileEncoding)
                    );
                } catch (IOException ex) {
                    if (this.ignoreResourceNotFound) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
                        }
                    } else {
                        throw ex;
                    }
                }
            }

            MutablePropertySources propertySources = new MutablePropertySources();
            propertySources.addLast(new PropertiesPropertySource("externalProperties", props));
            this.propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        }
    }
}
