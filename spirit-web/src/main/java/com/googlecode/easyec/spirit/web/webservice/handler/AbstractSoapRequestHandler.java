package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.webservice.Body;
import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.Header;
import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * 抽象类。<p/>
 * 支持SOAP请求的处理类。
 *
 * @author JunJie
 */
public abstract class AbstractSoapRequestHandler<T, E extends Envelope, H extends Header, B extends Body> implements HttpRequestHandler<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private EnvelopeFactory envelopeFactory;
    private String charset = "UTF-8";

    protected AbstractSoapRequestHandler(EnvelopeFactory envelopeFactory) {
        Assert.notNull(envelopeFactory, "EnvelopeFactory object is null.");
        this.envelopeFactory = envelopeFactory;
    }

    /**
     * 返回WebService的信封转换的工厂类
     *
     * @return <code>EnvelopeFactory</code>对象
     */
    public EnvelopeFactory getEnvelopeFactory() {
        return envelopeFactory;
    }

    @SuppressWarnings("unchecked")
    public T process(HttpClient httpClient) throws Exception {
        String xml = envelopeFactory.asXml(createEnvelope(createRequestHeader(), createRequestBody()));
        logger.debug("Request content: [{}].", xml);

        try {
            HttpResponse response = httpClient.execute(createHttpUriRequest(xml));
            int statusCode = response.getStatusLine().getStatusCode();
            logger.debug("Response of status code: [{}].", statusCode);

            if (200 == statusCode) {
                InputStream in = response.getEntity().getContent();

                try {
                    byte[] bs = IOUtils.toByteArray(in);
                    String ret = new String(bs, getCharset());
                    logger.debug("Response's content: [{}].", ret);

                    return doIn200((E) envelopeFactory.asEnvelope(ret));
                } finally {
                    IOUtils.closeQuietly(in);
                }
            } else {
                InputStream in = response.getEntity().getContent();

                try {
                    byte[] bs = IOUtils.toByteArray(in);
                    String ret = new String(bs, getCharset());

                    doInOtherCode(statusCode, ret);
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }

            return null;
        } catch (IOException e) {
            doInCatch(e);

            return null;
        }
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

    /**
     * 创建一个HTTP URI的请求对象
     *
     * @param xml SOAP数据文本
     * @return <code>HttpUriRequest</code>对象
     * @see HttpGet
     * @see HttpPost
     * @see HttpPut
     * @see HttpDelete
     */
    abstract protected HttpUriRequest createHttpUriRequest(String xml);

    /**
     * 创建SOAP请求的信封对象
     *
     * @param header <code>Header</code>对象
     * @param body   <code>Body</code>对象
     * @return <code>Envelope</code>对象
     * @see #createRequestHeader()
     * @see #createRequestBody()
     */
    abstract protected E createEnvelope(H header, B body);

    /**
     * 创建SOAP请求的头对象信息
     *
     * @return <code>Header</code>对象
     */
    abstract protected H createRequestHeader();

    /**
     * 创建SOAP请求的体对象信息
     *
     * @return <code>Body</code>对象
     */
    abstract protected B createRequestBody();
}
