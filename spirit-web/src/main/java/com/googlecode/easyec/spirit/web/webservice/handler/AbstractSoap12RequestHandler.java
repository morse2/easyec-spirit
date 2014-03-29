package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Body;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.nio.charset.Charset;

/**
 * 抽象类<br/>
 * 支持SOAP 1.2协议的请求处理类
 *
 * @author JunJie
 */
public abstract class AbstractSoap12RequestHandler<T>
    extends AbstractEnvelopeRequestHandler<T, Soap12, Soap12Header, Soap12Body> {

    protected AbstractSoap12RequestHandler(EnvelopeFactory envelopeFactory) {
        super(envelopeFactory);
    }

    protected AbstractSoap12RequestHandler() {
        // no op
    }

    @Override
    protected Soap12 createEnvelope(Soap12Header header, Soap12Body body) {
        return new Soap12(header, body);
    }

    @Override
    protected HttpUriRequest createHttpUriRequest(String xml) {
        HttpPost post = new HttpPost(getRequestUri());
        post.addHeader("SOAPAction", getSoapAction());
        post.addHeader("Content-Type", "text/xml; charset=" + getCharset());

        post.setEntity(new StringEntity(xml, Charset.forName(getCharset())));

        return post;
    }

    /**
     * 返回请求的远程地址
     *
     * @return 远程服务接口的地址
     */
    abstract public String getRequestUri();

    /**
     * 返回SOAP调用的Action名字
     *
     * @return Action名称
     */
    abstract public String getSoapAction();
}
