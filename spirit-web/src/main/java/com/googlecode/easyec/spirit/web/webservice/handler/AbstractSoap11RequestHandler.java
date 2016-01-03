package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11Body;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11Header;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * 抽象类<br/>
 * 支持SOAP 1.1协议的请求处理类
 *
 * @author JunJie
 */
public abstract class AbstractSoap11RequestHandler<T>
    extends AbstractEnvelopeRequestHandler<T, Soap11, Soap11Header, Soap11Body> {

    public AbstractSoap11RequestHandler(StreamObjectFactory objectFactory, String baseUri, Soap11 bean) {
        super(objectFactory, baseUri, bean);
    }

    @Override
    protected void handleHttpHeaders(HttpUriRequest request) throws Exception {
        super.handleHttpHeaders(request);

        request.addHeader("SOAPAction", getSoapAction());
        request.addHeader("Content-Type", "text/xml; charset=" + getEncoding());
    }

    @Override
    protected Soap11 createEnvelope(Soap11Header header, Soap11Body body) {
        return new Soap11(header, body);
    }

    /**
     * 返回SOAP调用的Action名字
     *
     * @return Action名称
     */
    abstract public String getSoapAction();
}
