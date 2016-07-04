package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static java.nio.charset.Charset.forName;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 基于XML数据流格式的对象工厂的抽象类
 *
 * @author JunJie
 * @since 0.6.4
 */
public abstract class AbstractXmlObjectFactory implements StreamObjectFactory {

    /* logger object */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Charset charset = forName("UTF-8");

    /**
     * 设置字符集
     *
     * @param charset 字符集
     */
    public void setCharset(String charset) {
        this.charset = forName(charset);
    }

    /**
     * 得到当前处理XML数据内容的字符集
     */
    public Charset getCharset() {
        return charset;
    }

    @Override
    public boolean accept(String contentType) {
        return isNotBlank(contentType) && contentType.endsWith("xml");
    }

    @Override
    public byte[] writeValue(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            doMarshal(obj, bos);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }

        return null;
    }

    @Override
    public <T> T readValue(byte[] xml, Class<T> classType) {
        InputStream bis = new ByteArrayInputStream(xml);

        try {
            return classType.cast(doUnmarshal(bis));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

        return null;
    }

    /**
     * 执行编码对象实例的方法
     *
     * @param obj 要被编组的对象
     * @param out 输出流对象
     * @throws Exception
     */
    abstract protected void doMarshal(Object obj, OutputStream out) throws Exception;

    /**
     * 执行还原编组的流数据到对象的方法
     *
     * @param in 流数据对象
     * @return 业务对象
     * @throws Exception
     */
    abstract protected Object doUnmarshal(InputStream in) throws Exception;
}
