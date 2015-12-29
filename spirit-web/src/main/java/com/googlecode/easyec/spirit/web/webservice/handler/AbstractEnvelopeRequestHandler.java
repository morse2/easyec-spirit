package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.httpcomponent.HttpRequestHandler;
import com.googlecode.easyec.spirit.web.webservice.Body;
import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.Header;
import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import com.googlecode.easyec.spirit.web.webservice.support.AbstractEnvelopeSupport;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * 抽象类。<p/>
 * 支持信封对象请求的处理类。
 *
 * @author JunJie
 */
public abstract class AbstractEnvelopeRequestHandler<T, E extends Envelope, H extends Header, B extends Body>
    extends AbstractEnvelopeSupport<T, E>
    implements HttpRequestHandler<T> {

    protected AbstractEnvelopeRequestHandler(StreamObjectFactory envelopeFactory) {
        setObjectFactory(envelopeFactory);
    }

    protected AbstractEnvelopeRequestHandler() {
        // default constructor
    }

    @SuppressWarnings("unchecked")
    public T process(HttpClient httpClient) throws Exception {
        String xml = asXml(createEnvelope(createRequestHeader(), createRequestBody()));
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

                    return doIn200(asEnvelope(ret));
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
