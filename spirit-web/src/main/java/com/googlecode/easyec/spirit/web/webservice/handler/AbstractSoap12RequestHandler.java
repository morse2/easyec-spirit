package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Body;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Header;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * 抽象类<br/>
 * 支持SOAP 1.2协议的请求处理类
 *
 * @author JunJie
 */
public abstract class AbstractSoap12RequestHandler<T>
    extends AbstractEnvelopeRequestHandler<T, Soap12, Soap12Header, Soap12Body> {

    public AbstractSoap12RequestHandler(StreamObjectFactory objectFactory, String baseUri, Soap12 bean) {
        super(objectFactory, baseUri, bean);
    }

    @Override
    protected void handleHttpHeaders(HttpUriRequest request) throws Exception {
        super.handleHttpHeaders(request);

        request.addHeader("SOAPAction", getSoapAction());
        request.addHeader("Content-Type", "text/xml; charset=" + getEncoding());
    }

    @Override
    protected Soap12 createEnvelope(Soap12Header header, Soap12Body body) {
        return new Soap12(header, body);
    }

    /**
     * 返回SOAP调用的Action名字
     *
     * @return Action名称
     */
    abstract public String getSoapAction();
}
