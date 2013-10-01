package com.googlecode.easyec.spirit.web.webservice.soap.v11;

import com.googlecode.easyec.spirit.web.webservice.Body;
import com.googlecode.easyec.spirit.web.webservice.BodyContent;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支持SOAP的消息体实现类。
 *
 * @author JunJie
 */
@XmlRootElement(name = "Body")
public class Soap11Body extends Body {

    private Soap11Body() { }

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     */
    public Soap11Body(BodyContent bodyContent) {
        super(bodyContent);
    }

    /**
     * 构造方法
     *
     * @param bodyContent 消息体内容对象
     * @param fault       消息体错误对象
     */
    public Soap11Body(BodyContent bodyContent, Soap11Fault fault) {
        super(bodyContent, fault);
    }
}
