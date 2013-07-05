package com.googlecode.easyec.spirit.web.soap.factory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

/**
 * 基于{@link JAXBContext}的SOAP工厂类实现。
 *
 * @author JunJie
 */
public class JaxbContextSoapFactory extends AbstractSoapFactory implements InitializingBean {

    protected JAXBContext jaxbContext;

    public JaxbContextSoapFactory(Class<?>[] classes) throws JAXBException {
        if (ArrayUtils.isEmpty(classes)) classes = new Class[0];

        jaxbContext = JAXBContext.newInstance(classes);
    }

    @Override
    protected String marshal(Object o) throws Exception {
        if (null == o) return null;

        StringWriter w = new StringWriter();
        jaxbContext.createMarshaller().marshal(o, new StreamResult(w));

        return w.toString();
    }

    @Override
    protected Object unmarshal(String xml) throws Exception {
        if (StringUtils.isBlank(xml)) return null;

        return jaxbContext.createUnmarshaller().unmarshal(new StreamSource(IOUtils.toInputStream(xml, "utf-8")));
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jaxbContext, "JAXBContext object cannot be null.");
    }
}
