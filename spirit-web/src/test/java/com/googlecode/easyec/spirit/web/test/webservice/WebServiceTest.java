package com.googlecode.easyec.spirit.web.test.webservice;

import com.googlecode.easyec.spirit.web.test.webservice.ui.JsonTest;
import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.TreeMap;

@ContextConfiguration(locations = "classpath:spring/test/applicationContext-*.xml")
public class WebServiceTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private StreamObjectFactory jsonObjectFactory;

    @Test
    public void parseJson() throws Exception {
        InputStream in = new ClassPathResource("samplejson/a.json").getInputStream();
        JsonTest json = jsonObjectFactory.readValue(IOUtils.toByteArray(in), JsonTest.class);
        System.out.println(json);
    }
}
