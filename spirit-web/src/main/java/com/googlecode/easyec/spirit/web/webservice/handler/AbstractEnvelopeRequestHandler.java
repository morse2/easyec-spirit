package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.Body;
import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.Header;
import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.util.Assert;

/**
 * 抽象类。<p/>
 * 支持信封对象请求的处理类。
 *
 * @author JunJie
 */
public abstract class AbstractEnvelopeRequestHandler<T, E extends Envelope, H extends Header, B extends Body>
    extends AbstractHttpPostRequestHandler<T, E> {

    public AbstractEnvelopeRequestHandler(StreamObjectFactory objectFactory, String baseUri, E bean) {
        super(objectFactory, baseUri, bean);
    }

    @Override
    protected HttpEntity createPostEntity() throws Exception {
        E envelope = createEnvelope(
            createRequestHeader(),
            createRequestBody()
        );

        Assert.notNull(envelope, "Envelope object is null.");

        return new ByteArrayEntity(
            getObjectFactory().writeValue(envelope)
        );
    }

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
