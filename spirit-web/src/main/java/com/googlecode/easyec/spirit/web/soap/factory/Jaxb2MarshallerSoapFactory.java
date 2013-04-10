package com.googlecode.easyec.spirit.web.soap.factory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;

/**
 * 基于{@link Jaxb2Marshaller}的SOAP工厂类实现。
 *
 * @author JunJie
 */
public class Jaxb2MarshallerSoapFactory extends AbstractSoapFactory implements InitializingBean {

    private Jaxb2Marshaller jaxb2Marshaller;

    public Jaxb2MarshallerSoapFactory(Jaxb2Marshaller jaxb2Marshaller) {
        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    @Override
    protected String marshal(Object o) throws Exception {
        if (null == o) return null;

        StringWriter w = new StringWriter();
        jaxb2Marshaller.marshal(o, new StreamResult(w));

        return w.toString();
    }

    @Override
    protected Object unmarshal(String xml) throws Exception {
        if (StringUtils.isBlank(xml)) return null;

        return jaxb2Marshaller.unmarshal(new StreamSource(IOUtils.toInputStream(xml, "utf-8")));
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jaxb2Marshaller, "Jaxb2Marshaller object cannot be null.");
    }
}
