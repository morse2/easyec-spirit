package com.googlecode.easyec.spirit.web.webservice.handler;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11Body;
import com.googlecode.easyec.spirit.web.webservice.soap.v11.Soap11Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.nio.charset.Charset;

/**
 * 抽象类<br/>
 * 支持SOAP 1.1协议的请求处理类
 *
 * @author JunJie
 */
public abstract class AbstractSoap11RequestHandler<T>
    extends AbstractEnvelopeRequestHandler<T, Soap11, Soap11Header, Soap11Body> {

    protected AbstractSoap11RequestHandler(StreamObjectFactory envelopeFactory) {
        super(envelopeFactory);
    }

    protected AbstractSoap11RequestHandler() {
        // no op
    }

    @Override
    protected Soap11 createEnvelope(Soap11Header header, Soap11Body body) {
        return new Soap11(header, body);
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
