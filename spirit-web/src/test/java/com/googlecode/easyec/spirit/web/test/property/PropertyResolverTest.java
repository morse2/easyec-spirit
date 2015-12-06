package com.googlecode.easyec.spirit.web.test.property;

import com.googlecode.easyec.spirit.web.utils.PropertyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;

/**
 * Created by JunJie on 12/6/15.
 */
@ContextConfiguration(locations = "classpath:spring/test/applicationContext-*.xml")
public class PropertyResolverTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private PropertyResolver propertyResolver;

    @Test
    public void getProperty() {
        String url = propertyResolver.getString("server.url");
        Assert.assertNotNull(url);
    }

    @Test
    public void getIntProperty() {
        Integer port = propertyResolver.getInt("server.port");
        Assert.assertEquals(8080, port.longValue());
    }

    @Test
    public void getNullProperty() {
        Integer i = propertyResolver.getInt("server.null");
        Assert.assertNull(i);
    }
}
