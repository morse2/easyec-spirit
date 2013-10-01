package com.googlecode.easyec.spirit.web.webservice.soap.v12;

import com.googlecode.easyec.spirit.web.webservice.Envelope;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * SOAP 1.2协议的实现类。
 *
 * @author JunJie
 */
@XmlRootElement(name = "Envelope")
public class Soap12 extends Envelope {

    private Soap12() { }

    /**
     * 构造方法
     *
     * @param body SOAP消息体
     */
    public Soap12(Soap12Body body) {
        super(body);
    }

    /**
     * 构造方法
     *
     * @param header SOAP消息头
     * @param body   SOAP消息体
     */
    public Soap12(Soap12Header header, Soap12Body body) {
        super(header, body);
    }
}
