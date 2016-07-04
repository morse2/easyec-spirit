package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 默认Jackson 2的对象工厂实现类
 *
 * @author JunJie
 */
public class DefaultJackson2ObjectFactory implements StreamObjectFactory {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJackson2ObjectFactory.class);
    private ObjectMapper objectMapper;

    public DefaultJackson2ObjectFactory(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper is null.");
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] writeValue(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    @Override
    public <T> T readValue(byte[] bs, Class<T> classType) {
        try {
            return objectMapper.readValue(bs, classType);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean accept(String contentType) {
        return StringUtils.isNotBlank(contentType) && contentType.endsWith("json");
    }
}
