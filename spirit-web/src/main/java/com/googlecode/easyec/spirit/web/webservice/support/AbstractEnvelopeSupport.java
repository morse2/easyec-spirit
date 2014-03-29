package com.googlecode.easyec.spirit.web.webservice.support;

import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 支持{@link Envelope}处理的基类
 *
 * @author JunJie
 */
public abstract class AbstractEnvelopeSupport<T, E extends Envelope> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private EnvelopeFactory envelopeFactory;
    private String charset = "UTF-8";

    protected AbstractEnvelopeSupport() {
        // protected constructor
    }

    /**
     * 返回当前使用的<code>EnvelopeFactory</code>实例
     *
     * @return <code>EnvelopeFactory</code>对象实例
     */
    public EnvelopeFactory getEnvelopeFactory() {
        return envelopeFactory;
    }

    /**
     * 设置当前信封对象需要使用的解析工厂
     *
     * @param envelopeFactory <code>EnvelopeFactory</code>对象实例
     */
    public void setEnvelopeFactory(EnvelopeFactory envelopeFactory) {
        Assert.notNull(envelopeFactory, "EnvelopeFactory object is null.");
        this.envelopeFactory = envelopeFactory;
    }

    /**
     * @see EnvelopeFactory#asEnvelope(String)
     */
    @SuppressWarnings("unchecked")
    protected E asEnvelope(String xml) {
        return (E) envelopeFactory.asEnvelope(xml);
    }

    /**
     * @see EnvelopeFactory#asXml(Envelope)
     */
    protected String asXml(Envelope envelope) {
        return envelopeFactory.asXml(envelope);
    }

    /**
     * 返回当前设定的字符集。
     * 默认为UTF-8
     *
     * @return 字符集
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置当前使用的字符集
     *
     * @param charset 字符集
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * 当调用目标地址时候发生错误，则此方法被调用
     *
     * @param e 异常对象
     */
    protected void doInCatch(Exception e) {
        logger.error(e.getMessage(), e);
    }

    /**
     * 当返回的状态代码不是200，则此方法被调用。
     *
     * @param statusCode HTTP调用返回的状态代码
     * @param s          HTTP调用返回的内容
     */
    protected void doInOtherCode(int statusCode, String s) {
        logger.debug("Status code of response is: [{}], return content is: [{}].", statusCode, s);
    }

    /**
     * 当前成功调用SOAP，并且返回状态为200的情况下，
     * 此方法则会被调用。
     *
     * @param o SOAP的信封对象
     * @return 返回业务对象
     */
    abstract protected T doIn200(E o);
}
