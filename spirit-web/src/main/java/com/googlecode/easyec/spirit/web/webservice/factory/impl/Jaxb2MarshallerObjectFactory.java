package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Spring OXM支持的XML数据转换工厂类
 *
 * @author JunJie
 */
public class Jaxb2MarshallerObjectFactory extends AbstractXmlObjectFactory {

    private Jaxb2Marshaller jaxb2Marshaller;

    public Jaxb2MarshallerObjectFactory(Jaxb2Marshaller jaxb2Marshaller) {
        Assert.notNull(jaxb2Marshaller, "Jaxb2Marshaller object is null.");

        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    @Override
    protected void doMarshal(Object obj, OutputStream out) throws Exception {
        jaxb2Marshaller.marshal(obj, new StreamResult(out));
    }

    @Override
    protected Object doUnmarshal(InputStream in) throws Exception {
        return jaxb2Marshaller.unmarshal(new StreamSource(in));
    }
}
