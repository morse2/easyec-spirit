package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * JAXB2.0支持的XML数据转换工厂类
 *
 * @author JunJie
 */
public class JaxbContextObjectFactory extends AbstractXmlObjectFactory {

    private JAXBContext jaxbContext;

    public JaxbContextObjectFactory(Class[] classes) throws JAXBException {
        this(JAXBContext.newInstance(classes));
    }

    public JaxbContextObjectFactory(JAXBContext jaxbContext) {
        Assert.notNull(jaxbContext, "JAXBContext object cannot be null.");

        this.jaxbContext = jaxbContext;
    }

    /**
     * 返回当前工厂类已初始化的JAXB上下文对象实例
     *
     * @return <code>JAXBContext</code>对象
     */
    protected JAXBContext getJAXBContext() {
        return jaxbContext;
    }

    @Override
    protected void doMarshal(Object obj, OutputStream out) throws Exception {
        getJAXBContext().createMarshaller().marshal(obj, out);
    }

    @Override
    protected Object doUnmarshal(InputStream in) throws Exception {
        return getJAXBContext().createUnmarshaller().unmarshal(in);
    }
}
