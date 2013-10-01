package com.googlecode.easyec.spirit.web.webservice.soap.v11;

import com.googlecode.easyec.spirit.web.webservice.Envelope;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * SOAP 1.1协议的实现类。
 *
 * @author JunJie
 */
@XmlRootElement(name = "Envelope")
public class Soap11 extends Envelope {

    private Soap11() { }

    /**
     * 构造方法
     *
     * @param body SOAP消息体
     */
    public Soap11(Soap11Body body) {
        super(body);
    }

    /**
     * 构造方法
     *
     * @param header SOAP消息头
     * @param body   SOAP消息体
     */
    public Soap11(Soap11Header header, Soap11Body body) {
        super(header, body);
    }
}
