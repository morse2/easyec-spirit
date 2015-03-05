package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import com.googlecode.easyec.spirit.web.webservice.factory.XmlObjectFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.Charset.forName;

/**
 * JAXB2.0支持的信封数据转换工厂类
 *
 * @author JunJie
 */
public class JaxbContextEnvelopeFactory implements EnvelopeFactory, XmlObjectFactory {

    private static final Logger logger = LoggerFactory.getLogger(JaxbContextEnvelopeFactory.class);

    private Charset charset = forName("UTF-8");
    private JAXBContext jaxbContext;

    public JaxbContextEnvelopeFactory(Class[] classes) throws JAXBException {
        this(JAXBContext.newInstance(classes));
    }

    public JaxbContextEnvelopeFactory(JAXBContext jaxbContext) {
        Assert.notNull(jaxbContext, "JAXBContext object cannot be null.");

        this.jaxbContext = jaxbContext;
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     */
    public void setCharset(String charset) {
        this.charset = forName(charset);
    }

    public String asXml(Envelope envelope) {
        return asXml((Object) envelope);
    }

    public Envelope asEnvelope(String xml) {
        Object obj = asObject(xml);
        if (obj != null) {
            Assert.isInstanceOf(Envelope.class, obj);

            return (Envelope) obj;
        }

        return null;
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
    public String asXml(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            getJAXBContext().createMarshaller().marshal(obj, bos);
            return new String(bos.toByteArray(), charset);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }

        return null;
    }

    @Override
    public Object asObject(String xml) {
        InputStream bis = IOUtils.toInputStream(xml, charset);

        try {
            return getJAXBContext().createUnmarshaller().unmarshal(bis);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

        return null;
    }
}
